package in.apt.assetmanager.service.impl;

import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.models.AssetModel;
import in.apt.assetmanager.models.AssetResponse;
import in.apt.assetmanager.models.Response;
import in.apt.assetmanager.service.AssetService;
import in.apt.assetmanager.service.ObjectStorageService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileServiceImplTest {

    @Mock
    AssetService assetService;

    @Mock
    ObjectStorageService objectStorageService;

    @InjectMocks
    FileServiceImpl fileService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    MultipartFile file = new MockMultipartFile("file_1", "orig_file_1", "images/jpeg", "".getBytes());

    AssetModel assetModel = AssetModel.builder().id("123456").status("Active").timestamp("1534865325873").build();




    @Test
    public void test_uploadAsset() throws AssetManagerException {
        ReflectionTestUtils.setField(fileService,"maxAssetSize",5242880L);
        String name = "orig_file_1";
        String s3Name = new Date().getTime() + "_" + name;
        when(assetService.saveAsset(eq(file), anyString())).thenReturn(assetModel);
        doNothing().when(objectStorageService).upload(anyString(),any());
        AssetResponse response = AssetResponse.builder().message("OK").status("UPLOADED").id("123456").build();
        Response actualResponse = fileService.uploadAsset(file);
        Assert.assertEquals(response, actualResponse);
    }

    @Test(expected = AssetManagerException.class)
    public void test_failedUploadAsset() throws AssetManagerException {
        ReflectionTestUtils.setField(fileService,"maxAssetSize",5242880L);
        byte[] hugeFile = new byte[6242880];
        MockMultipartFile mockMultipartFile = new MockMultipartFile("huge_file", hugeFile);
        fileService.uploadAsset(mockMultipartFile);
    }

    @Test
    public void test_fetchAssetWithTTL() throws AssetManagerException {
        when(assetService.fetchActiveAssetLocation(123456L)).thenReturn("1234_file_1");
        when(objectStorageService.getExpirableLocation("1234_file_1", "120")).thenReturn("http://somebucket.url/1234_file_1?ttl=120");
        AssetResponse expectedResponse = AssetResponse.builder().downloadURL("http://somebucket.url/1234_file_1?ttl=120").message("OK").build();
        Response response = fileService.fetchAssetUrlWithTTL("123456", "120");
        Assert.assertEquals(expectedResponse, response);
    }

    @Test(expected = AssetManagerException.class)
    public void test_invalidId_fetchAssetWithTTL() throws AssetManagerException {
        Response reponse = fileService.fetchAssetUrlWithTTL("NaN", "120");
    }

    @Test(expected = AssetManagerException.class)
    public void test_invalidTTL_fetchAssetWithTTL() throws AssetManagerException {
        Response response = fileService.fetchAssetUrlWithTTL("123456", "TTL");
    }

    @Test(expected = AssetManagerException.class)
    public void test_assetNotFound_fetchAssetWithTTL() throws AssetManagerException {
        when(assetService.fetchActiveAssetLocation(123456L)).thenThrow(AssetManagerException.class);
        Response response = fileService.fetchAssetUrlWithTTL("123456", "120");
    }

    @Test
    public void test_updateAssetStatus() throws AssetManagerException {
        when(assetService.updateAssetStatus(anyLong(),any())).thenReturn(assetModel);
        AssetModel request = AssetModel.builder().status("ARCHIVED").build();
        Response actualResponse = fileService.updateAssetStatus("123456", request);
        Response expectedResponse = new Response("OK");
        Assert.assertEquals(expectedResponse, actualResponse);
    }

    @Test(expected = AssetManagerException.class)
    public void test_invalidIdentifier_updateAssetStatus() throws AssetManagerException {
        Response response = fileService.updateAssetStatus("NaN", assetModel);
    }


}
