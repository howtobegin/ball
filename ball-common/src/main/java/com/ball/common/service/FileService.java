package com.ball.common.service;

import com.ball.base.exception.BizErr;
import com.ball.base.exception.FileNotFoundException;
import com.ball.base.util.IDCreator;
import com.ball.common.exception.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author littlehow
 */
@Service
@Slf4j
public class FileService {
    @Value("${file.path:/data/file}")
    private String rootPath;

    @Value("${file.url:}")
    private String url;

    private DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 保存文件
     * @param file -
     */
    public String save(MultipartFile file) {
        String suffixName = "";
        String fileName = file.getOriginalFilename();
        if (fileName != null) {
            suffixName = fileName.substring(fileName.lastIndexOf("."));
        }
        String today = LocalDate.now().format(date);
        String name = today + "/" + IDCreator.get() + suffixName;
        File dest = new File(rootPath + name);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return name;
        } catch (Exception e) {
            log.error("save file error", e);
            throw new BizErr(CommonErrorCode.SAVE_FILE_ERROR);
        }
    }

    /**
     * 读取文件
     * @param name -
     * @return -
     */
    public byte[] read(String name) {
        File file = new File(rootPath + name);
        if (!file.isFile()) {
            throw new FileNotFoundException(name);
        }
        try {
            return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
        } catch (Exception e) {
            log.error("get file error", e);
            throw new BizErr(CommonErrorCode.READ_FILE_ERROR);
        }
    }

    /**
     * 获取文件地址
     * @param name -
     * @return -
     */
    public String getUrl(String name) {
        if (StringUtils.hasText(name)) {
            return url + name;
        }
        return name;
    }
}
