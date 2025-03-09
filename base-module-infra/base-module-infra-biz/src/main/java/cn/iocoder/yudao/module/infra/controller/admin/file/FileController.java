package cn.iocoder.yudao.module.infra.controller.admin.file;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FilePageReqVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileRespVO;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileUploadReqVO;
import cn.iocoder.yudao.module.infra.dal.dataobject.file.FileDO;
import cn.iocoder.yudao.module.infra.service.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import apijson.JSON;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 文件存储")
@RestController
@RequestMapping("/infra/file")
@Validated
@Slf4j
public class FileController {

    @Resource
    private FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "上传文件")
    @OperateLog(logArgs = false) // 上传文件，没有记录操作日志的必要
    public CommonResult<String> uploadFile(FileUploadReqVO uploadReqVO) throws Exception {
        MultipartFile file = uploadReqVO.getFile();
        String path = uploadReqVO.getPath();
        return success(fileService.createFile(file.getOriginalFilename(), path, IoUtil.readBytes(file.getInputStream())));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('infra:file:delete')")
    public CommonResult<Boolean> deleteFile(@RequestParam("id") Long id) throws Exception {
        fileService.deleteFile(id);
        return success(true);
    }

    @GetMapping("/{configId}/get/**")
    @PermitAll
    @Operation(summary = "下载文件")
    @Parameter(name = "configId", description = "配置编号",  required = true)
    public void getFileContent(HttpServletRequest request,
                               HttpServletResponse response,
                               @PathVariable("configId") Long configId) throws Exception {
        // 获取请求的路径
        String path = StrUtil.subAfter(request.getRequestURI(), "/get/", false);
        if (StrUtil.isEmpty(path)) {
            throw new IllegalArgumentException("结尾的 path 路径必须传递");
        }

        // 读取内容
        byte[] content = fileService.getFileContent(configId, path);
        if (content == null) {
            log.warn("[getFileContent][configId({}) path({}) 文件不存在]", configId, path);
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }
        ServletUtils.writeAttachment(response, path, content);
    }

    @GetMapping("/page")
    @Operation(summary = "获得文件分页")
    @PreAuthorize("@ss.hasPermission('infra:file:query')")
    public CommonResult<PageResult<FileRespVO>> getFilePage(@Valid FilePageReqVO pageVO) {
        PageResult<FileDO> pageResult = fileService.getFilePage(pageVO);
        return success(BeanUtils.toBean(pageResult, FileRespVO.class));
    }

    @GetMapping("/detail")
    @Operation(summary = "获取文件详细信息")
    public CommonResult<List<Map<String,String>>> getFileInfo(@RequestParam("url") String url) throws Exception {
        List<Map<String,String>> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(url)){
            for(String ss:url.split(",")){
                if (StringUtils.isNotEmpty(ss)){
                    String path = ss.substring(ss.lastIndexOf("/")+1);
                    String fileName = "";
                    if (StringUtils.isNotEmpty(path)){
                        fileName = fileService.getFileName(path);
                    }
                    if (StringUtils.isEmpty(fileName)){
                        fileName = path;
                    }
                    Map<String,String> map = new HashMap<>();
                    map.put("name",fileName);
                    map.put("url",ss);
                    list.add(map);
                }
            }
        }
        return success(list);
    }

    /**
     * 上传编辑器文件
     * @param request
     * @param type
     * @return
     */
    public CommonResult uploadFile(HttpServletRequest request,String type){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
        List<Map<String, Object>> mapList = new ArrayList<>();
        try{
            //获取到正真上传的文件内容
            Set<Map.Entry<String, List<MultipartFile>>> entries = fileMap.entrySet();
            Iterator<Map.Entry<String, List<MultipartFile>>> iterator = entries.iterator();
            Map.Entry<String, List<MultipartFile>> next = iterator.next();
            List<MultipartFile> values = next.getValue();
            for (MultipartFile file : values) {
                String path = fileService.createFile(file.getOriginalFilename(), null, IoUtil.readBytes(file.getInputStream()));
                if (StringUtils.isNotEmpty(path)){
                    String originalFilename = file.getOriginalFilename();
                    String fileExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("state", "SUCCESS");
                    //图片地址，/ueditor/jsp/config.json配置中配置的图片前缀为"",所以这里返回完成地址
                    map.put("url", path);
                    map.put("title", file.getName());
                    map.put("original",file.getOriginalFilename());
                    map.put("type", fileExt);
                    map.put("size", file.getSize());

                    mapList.add(map);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (mapList.size()==0){
            Map<String, Object> map = new HashMap<>();
            map.put("state", "ERROR");
            map.put("message", "上传失败");
            mapList.add(map);
        }

        if (type.equalsIgnoreCase("listfile") || type.equalsIgnoreCase("listimage")){
            return CommonResult.success(mapList);
        }else{
            return CommonResult.success(mapList.get(0));
        }
    }

    /**
     * 上传富文本编辑器文件
     */
    @RequestMapping("/upload_editor_file")
    public void uploadEditorFile(HttpServletRequest request, HttpServletResponse response, String action) {
        Map<String, Object> map = new HashMap<>();
        try {
            switch (action){
                case "uploadscrawl":
                case "uploadvideo":
                {
                    CommonResult responseResult = uploadFile(request,action);
                    if (responseResult.isSuccess()){
                        map = (Map<String, Object>) responseResult.getData();
                        break;
                    }
                }
                case "uploadfile":
                case "uploadimage":{
                    int uploadType = 0;
                    if ("uploadimage".equals(action)){
                        uploadType = 1;
                    }else if("uploadvideo".equals(action)){
                        uploadType = 3;
                    }else if("uploadfile".equals(action)){
                        uploadType = 2;
                    }

                    request.setAttribute("uploadType",uploadType);
                    CommonResult responseResult = uploadFile(request,action);
                    if (responseResult.isSuccess()){
                        map = (Map<String, Object>) responseResult.getData();
                        break;
                    }
                }
                //文件列表
                case "listfile":{
                    List< Map<String, Object>> filelist = new ArrayList<>();
                    CommonResult responseResult = uploadFile(request,action);
                    if (responseResult.isSuccess()){
                        filelist = (List< Map<String, Object>>) responseResult.getData();

//                        返回json
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        response.setHeader("Access-Control-Allow-Origin", "*");
                        PrintWriter out = null;
                        try {
                            out = response.getWriter();
                            out.append(JSON.toJSONString(filelist));
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (out != null) {
                                out.close();
                            }
                        }
                    }

                    return ;
                }
                //图片列表
                case "listimage":{
                    break;
                }
                case "config":{
                    try {
                        String configJson = "/* 前后端通信相关的配置,注释只允许使用多行方式,此文件修改及生效,不用重启服务 */\n" +
                                "{\n" +
                                "    /* 上传图片配置项 */\n" +
                                "    \"imageActionName\": \"uploadimage\", /* 执行上传图片的action名称 */\n" +
                                "    \"imageFieldName\": \"upfile\", /* 提交的图片表单名称 */\n" +
                                "    \"imageMaxSize\": 2048000000, /* 上传大小限制，单位B */\n" +
                                "    \"imageAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], /* 上传图片格式显示 */\n" +
                                "    \"imageCompressEnable\": false, /* 是否压缩图片,默认是true */\n" +
                                "    \"imageCompressBorder\": 800, /* 图片压缩最大宽度限制 */\n" +
                                "    \"imageInsertAlign\": \"none\", /* 插入的图片浮动方式 */\n" +
                                "    \"imageUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                                "    \"imagePathFormat\": \"/userfiles/images/{yyyy}{mm}{dd}/{filename}_${time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                                "                                /* {filename} 会替换成原文件名,配置这项需要注意中文乱码问题 */\n" +
                                "                                /* {rand:6} 会替换成随机数,后面的数字是随机数的位数 */\n" +
                                "                                /* {time} 会替换成时间戳 */\n" +
                                "                                /* {yyyy} 会替换成四位年份 */\n" +
                                "                                /* {yy} 会替换成两位年份 */\n" +
                                "                                /* {mm} 会替换成两位月份 */\n" +
                                "                                /* {dd} 会替换成两位日期 */\n" +
                                "                                /* {hh} 会替换成两位小时 */\n" +
                                "                                /* {ii} 会替换成两位分钟 */\n" +
                                "                                /* {ss} 会替换成两位秒 */\n" +
                                "                                /* 非法字符 \\ : * ? \" < > | */\n" +
                                "                                /* 具请体看线上文档: fex.baidu.com/ueditor/#use-format_upload_filename */\n" +
                                "\n" +
                                "    /* 涂鸦图片上传配置项 */\n" +
                                "    \"scrawlActionName\": \"uploadscrawl\", /* 执行上传涂鸦的action名称 */\n" +
                                "    \"scrawlFieldName\": \"upfile\", /* 提交的图片表单名称 */\n" +
                                "    \"scrawlPathFormat\": \"/userfiles/images/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                                "    \"scrawlMaxSize\": 2048000000, /* 上传大小限制，单位B */\n" +
                                "    \"scrawlUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                                "    \"scrawlInsertAlign\": \"none\",\n" +
                                "\n" +
                                "    /* 截图工具上传 */\n" +
                                "    \"snapscreenActionName\": \"uploadimage\", /* 执行上传截图的action名称 */\n" +
                                "    \"snapscreenPathFormat\": \"/userfiles/images/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                                "    \"snapscreenUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                                "    \"snapscreenInsertAlign\": \"none\", /* 插入的图片浮动方式 */\n" +
                                "\n" +
                                "    /* 抓取远程图片配置 */\n" +
                                "    \"catcherLocalDomain\": [\"127.0.0.1\", \"localhost\", \"img.baidu.com\"],\n" +
                                "    \"catcherActionName\": \"catchimage\", /* 执行抓取远程图片的action名称 */\n" +
                                "    \"catcherFieldName\": \"source\", /* 提交的图片列表表单名称 */\n" +
                                "    \"catcherPathFormat\": \"/userfiles/images/{yyyy}{mm}{dd}/{time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                                "    \"catcherUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                                "    \"catcherMaxSize\": 2048000000, /* 上传大小限制，单位B */\n" +
                                "    \"catcherAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], /* 抓取图片格式显示 */\n" +
                                "\n" +
                                "    /* 上传视频配置 */\n" +
                                "    \"videoActionName\": \"uploadvideo\", /* 执行上传视频的action名称 */\n" +
                                "    \"videoFieldName\": \"upfile\", /* 提交的视频表单名称 */\n" +
                                "    \"videoPathFormat\": \"/userfiles/videos/{yyyy}{mm}{dd}/{filename}_${time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                                "    \"videoUrlPrefix\": \"\", /* 视频访问路径前缀 */\n" +
                                "    \"videoMaxSize\": 2048000000, /* 上传大小限制，单位B，默认100MB */\n" +
                                "    \"videoAllowFiles\": [\n" +
                                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\",\".m4v\", \".webm\", \".mp3\", \".wav\", \".mid\"], /* 上传视频格式显示 */\n" +
                                "\n" +
                                "    /* 上传文件配置 */\n" +
                                "    \"fileActionName\": \"uploadfile\", /* controller里,执行上传视频的action名称 */\n" +
                                "    \"fileFieldName\": \"upfile\", /* 提交的文件表单名称 */\n" +
                                "    \"filePathFormat\": \"/userfiles/files/{yyyy}{mm}{dd}/{filename}_${time}{rand:6}\", /* 上传保存路径,可以自定义保存路径和文件名格式 */\n" +
                                "    \"fileUrlPrefix\": \"\", /* 文件访问路径前缀 */\n" +
                                "    \"fileMaxSize\": 5120000000, /* 上传大小限制，单位B，默认50MB */\n" +
                                "    \"fileAllowFiles\": [\n" +
                                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                                "        \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\", \".ipa\", \".apk\",\n" +
                                "        \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"\n" +
                                "    ], /* 上传文件格式显示 */\n" +
                                "\n" +
                                "    /* 列出指定目录下的图片 */\n" +
                                "    \"imageManagerActionName\": \"listimage\", /* 执行图片管理的action名称 */\n" +
                                "    \"imageManagerListPath\": \"/userfiles/images/\", /* 指定要列出图片的目录 */\n" +
                                "    \"imageManagerListSize\": 100, /* 每次列出文件数量 */\n" +
                                "    \"imageManagerUrlPrefix\": \"\", /* 图片访问路径前缀 */\n" +
                                "    \"imageManagerInsertAlign\": \"none\", /* 插入的图片浮动方式 */\n" +
                                "    \"imageManagerAllowFiles\": [\".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\"], /* 列出的文件类型 */\n" +
                                "\n" +
                                "    /* 列出指定目录下的文件 */\n" +
                                "    \"fileManagerActionName\": \"listfile\", /* 执行文件管理的action名称 */\n" +
                                "    \"fileManagerListPath\": \"/userfiles/files/\", /* 指定要列出文件的目录 */\n" +
                                "    \"fileManagerListSize\": 100, /* 每次列出文件数量 */\n" +
                                "    \"fileManagerUrlPrefix\": \"\", /* 文件访问路径前缀 */\n" +
                                "    \"fileManagerAllowFiles\": [\n" +
                                "        \".png\", \".jpg\", \".jpeg\", \".gif\", \".bmp\",\n" +
                                "        \".flv\", \".swf\", \".mkv\", \".avi\", \".rm\", \".rmvb\", \".mpeg\", \".mpg\",\n" +
                                "        \".ogg\", \".ogv\", \".mov\", \".wmv\", \".mp4\", \".webm\", \".mp3\", \".wav\", \".mid\",\n" +
                                "        \".rar\", \".zip\", \".tar\", \".gz\", \".7z\", \".bz2\", \".cab\", \".iso\",\n" +
                                "        \".doc\", \".docx\", \".xls\", \".xlsx\", \".ppt\", \".pptx\", \".pdf\", \".txt\", \".md\", \".xml\"\n" +
                                "    ] /* 列出的文件类型 */\n" +
                                "\n" +
                                "}";
                        String exec = JSON.parseObject(configJson).toJSONString();
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json; charset=utf-8");
                        response.setHeader("Access-Control-Allow-Origin", "*");
                        PrintWriter out = response.getWriter();
                        out.append(exec);
                        out.flush();
                        out.close();
                        return ;
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            //返回json
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            PrintWriter out = null;
            try {
                out = response.getWriter();
                out.append(JSON.toJSONString(map));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
