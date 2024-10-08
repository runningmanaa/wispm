package com.laigeoffer.pmhub.project.service.file;

import com.laigeoffer.pmhub.base.core.core.domain.model.LoginUser;
import com.laigeoffer.pmhub.project.domain.vo.project.file.FileVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zw
 * @date 2023-01-03 17:22
 */
public abstract class UploadAbstractExecutor {
    public abstract FileVO upload(LoginUser user, MultipartFile file, String id) throws Exception;
}
