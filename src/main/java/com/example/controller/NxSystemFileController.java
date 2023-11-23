package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.entity.NxSystemFileInfo;
import com.example.exception.CustomException;
import com.example.service.NxSystemFileInfoService;
import com.github.pagehelper.PageInfo;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class NxSystemFileController {

    private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/resources/static/file/";

    @Resource
    private NxSystemFileInfoService nxSystemFileInfoService;

    /**
     * 上传文件
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file, HttpServletRequest request) throws IOException {
        String originName = file.getOriginalFilename();
        // 1. 先查询有没有相同名称的文件
//        NxSystemFileInfo fileInfo = nxSystemFileInfoService.findByFileName(name);
//        if (fileInfo != null) {
//            throw new CustomException("1001", "文件名：\"" + name + "\"已存在");
//        }
        // 文件名加个时间戳
        String fileName = FileUtil.mainName(originName) + System.currentTimeMillis() + "." + FileUtil.extName(originName);

        // 2. 文件上传
        FileUtil.writeBytes(file.getBytes(), BASE_PATH + fileName);

        // 3. 信息入库，获取文件id
        NxSystemFileInfo info = new NxSystemFileInfo();
        info.setOriginName(originName);
        info.setFileName(fileName);
        NxSystemFileInfo addInfo = nxSystemFileInfoService.add(info);
        if (addInfo != null) {
            return Result.success(addInfo);
        } else {
            return Result.error("4001", "上传失败");
        }
    }

    /**
     * 上传通知公告中的文件
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @PostMapping("/notice/upload")
    public Result<Map<String, String>> noticeUpload(MultipartFile file, HttpServletRequest request) throws IOException {
        //接收前端传入的MultipartFile文件对象，将文件保存到服务器的指定路径并缩小尺寸
        //然后将文件信息存入数据库中，并返回文件的下载链接和标题信息
        String originName = file.getOriginalFilename();
        // 文件名加个时间戳
        String fileName = FileUtil.mainName(originName) + System.currentTimeMillis() + "." + FileUtil.extName(originName);
        // 2. 缩小尺寸
        FileUtil.mkdir(BASE_PATH);
        Thumbnails.of(file.getInputStream()).width(400).toFile(BASE_PATH + fileName);

        // 3. 信息入库，获取文件id
        NxSystemFileInfo info = new NxSystemFileInfo();
        info.setOriginName(originName);
        info.setFileName(fileName);
        NxSystemFileInfo addInfo = nxSystemFileInfoService.add(info);

        Map<String, String> map = new HashMap<>(2);
        map.put("src", "/files/download/" + addInfo.getId());
        map.put("title", originName);
        return Result.success(map);
    }

    /**
     * 分页获取文件信息列表
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/page/{name}")
    public Result<PageInfo<NxSystemFileInfo>> filePage(@PathVariable String name,
                                                       @RequestParam(defaultValue = "1") Integer pageNum,
                                                       @RequestParam(defaultValue = "10") Integer pageSize) {

        PageInfo<NxSystemFileInfo> pageInfo = nxSystemFileInfoService.findPage(name, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 下载文件
     * @param id
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/{id}")
    public void download(@PathVariable String id, HttpServletResponse response) throws IOException {
        //根据传入的id参数从数据库中获取文件信息，然后从服务器指定路径读取文件内容并写入HttpServletResponse的输出流中，实现文件的下载
        if ("null".equals(id)) {
            throw new CustomException("1001", "您未上传文件");
        }
        NxSystemFileInfo nxSystemFileInfo = nxSystemFileInfoService.findById(Long.parseLong(id));
        if (nxSystemFileInfo == null) {
            throw new CustomException("1001", "未查询到该文件");
        }
        byte[] bytes = FileUtil.readBytes(BASE_PATH + nxSystemFileInfo.getFileName());
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(nxSystemFileInfo.getOriginName(), "UTF-8"));
        response.addHeader("Content-Length", "" + bytes.length);
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(bytes);
        toClient.flush();
        toClient.close();
    }

    /**
     * 删除文件
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteFile(@PathVariable String id) {
        //根据传入的id参数从数据库中获取文件信息，然后先从服务器指定路径删除文件，再从数据库中删除文件信息
        NxSystemFileInfo nxSystemFileInfo = nxSystemFileInfoService.findById(Long.parseLong(id));
        if (nxSystemFileInfo == null) {
            throw new CustomException("1001", "未查询到该文件");
        }
        String name = nxSystemFileInfo.getFileName();
        // 先删除文件
        FileUtil.del(new File(BASE_PATH + name));
        // 再删除表记录
        nxSystemFileInfoService.delete(Long.parseLong(id));

        return Result.success();
    }

    /**
     * 根据id获取文件信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<NxSystemFileInfo> getById(@PathVariable String id) {
        //根据传入的id参数从数据库中获取文件信息，并检查文件是否存在，然后返回文件信息
        NxSystemFileInfo nxSystemFileInfo = nxSystemFileInfoService.findById(Long.parseLong(id));
        if (nxSystemFileInfo == null) {
            throw new CustomException("1001", "数据库未查到此文件");
        }
        try {
            FileUtil.readBytes(BASE_PATH + nxSystemFileInfo.getFileName());
        } catch (Exception e) {
            throw new CustomException("1001", "此文件已被您删除");
        }
        return Result.success(nxSystemFileInfo);
    }
}
