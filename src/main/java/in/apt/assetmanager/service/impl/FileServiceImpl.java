package in.apt.assetmanager.service.impl;

import in.apt.assetmanager.constants.Constants;
import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.exception.ErrorConstants;
import in.apt.assetmanager.models.AssetModel;
import in.apt.assetmanager.models.AssetResponse;
import in.apt.assetmanager.models.Response;
import in.apt.assetmanager.service.AssetService;
import in.apt.assetmanager.service.FileService;
import in.apt.assetmanager.service.ObjectStorageService;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private AssetService assetService;

    @Autowired
    private ObjectStorageService objectStorageService;

    @Value("${asset.max.size:5242880}")
    private long maxAssetSize;


    @Override
    public Response uploadAsset(MultipartFile fileMeta) throws AssetManagerException {
        try {
            validateAsset(fileMeta);
            File file = convertMultipartToFile(fileMeta);
            String fileName = generateFileName(fileMeta);
            AssetModel assetModel = assetService.saveAsset(fileMeta, fileName);
            objectStorageService.upload(fileName,file);
            file.delete();
            return AssetResponse.builder().id(assetModel.getId())
                    .status(Constants.UPLOADED)
                    .message(Constants.OK)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AssetManagerException(ErrorConstants.FAILED_TO_READ_FILE, HttpStatus.BAD_REQUEST);
        }
    }

    private void validateAsset(MultipartFile fileMeta) throws AssetManagerException {
        if (fileMeta == null) throw new AssetManagerException(ErrorConstants.FAILED_TO_READ_FILE, HttpStatus.BAD_REQUEST);
        if (fileMeta.getSize() >= maxAssetSize) throw new AssetManagerException(ErrorConstants.EXCEEDS_ALLOWED_SIZE, HttpStatus.BAD_REQUEST);
    }

    private File convertMultipartToFile(MultipartFile fileMeta) throws IOException {
        File file = new File(fileMeta.getOriginalFilename());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(fileMeta.getBytes());
        fileOutputStream.close();
        return file;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    @Override
    public Response fetchAssetUrlWithTTL(String id, String ttl) throws AssetManagerException {
        validateParams(id);
        if (ttl!=null) validateParams(ttl); //TTL is optional param in request
        String url = fetchAssetUrl(id);
        String expirableLocation = objectStorageService.getExpirableLocation(url, ttl);
        return AssetResponse.builder().downloadURL(expirableLocation).message(Constants.OK).build();
    }

    private String fetchAssetUrl(String id) throws AssetManagerException {
        return assetService.fetchActiveAssetLocation(Long.parseLong(id));
    }

    private void validateParams(String... nums) throws AssetManagerException {
        for (String num: nums) {
            if (!NumberUtils.isDigits(num)) throw new AssetManagerException(ErrorConstants.FOUND_NON_NUMERICS, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Response updateAssetStatus(String assetId, AssetModel asset) throws AssetManagerException {
        validateParams(assetId);
        assetService.updateAssetStatus(Long.parseLong(assetId), asset);
        return new Response(Constants.OK);
    }
}
