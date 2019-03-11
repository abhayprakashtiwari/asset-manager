package in.apt.assetmanager.service.impl;

import in.apt.assetmanager.entity.Asset;
import in.apt.assetmanager.exception.AssetManagerException;
import in.apt.assetmanager.models.AssetModel;
import in.apt.assetmanager.repository.AssetRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssetServiceTest {

    @Mock
    AssetRepository assetRepository;

    @InjectMocks
    AssetServiceImpl assetService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    MultipartFile file = new MockMultipartFile("file_1", "orig_file_1", "images/jpeg", "someImage".getBytes());

    Asset asset = Asset.builder().id(123456L).name("file_1").timestamp(new Timestamp(new Date().getTime())).size(123L).url("1234_file_1").status("ACTIVE").type("images/jpeg").build();


    @Test
    public void test_saveAsset(){
        when(assetRepository.save(any())).thenReturn(asset);
        AssetModel assetModel = new AssetModel();
        assetModel.setId("123456");
        assetModel.setTimestamp(Long.toString(new Timestamp(new Date().getTime()).getTime()));
        assetModel.setStatus("ACTIVE");
        AssetModel savedAsset = assetService.saveAsset(file, "file_1");
        Assert.assertEquals(assetModel.getId(), savedAsset.getId());
        Assert.assertEquals(assetModel.getStatus(), savedAsset.getStatus());
    }

    @Test
    public void test_updateAssetStatus() throws AssetManagerException {
        when(assetRepository.findAssetById(123456L)).thenReturn(asset);
        asset.setStatus("ARCHIVED");
        when(assetRepository.save(asset)).thenReturn(asset);
        AssetModel archivedRequest = AssetModel.builder().status("ARCHIVED").build();
        AssetModel updatedAssetModel = assetService.updateAssetStatus(123456L, archivedRequest);
        Assert.assertEquals(updatedAssetModel.getStatus(), "ARCHIVED");
        Assert.assertEquals(updatedAssetModel.getId(), "123456");
    }

    @Test(expected = AssetManagerException.class)
    public void test_updateAssetInvalidStatus() throws AssetManagerException {
        AssetModel invalidRequest = AssetModel.builder().status("INACTIVE").build();
        assetService.updateAssetStatus(123456L, invalidRequest);
    }

    @Test(expected = AssetManagerException.class)
    public void test_updateArchivedAsset_shouldThrowException() throws AssetManagerException {
        when(assetRepository.findAssetById(123456L)).thenReturn(null);
        AssetModel archivedRequest = AssetModel.builder().status("ARCHIVED").build();
        assetService.updateAssetStatus(123456L, archivedRequest);
    }

    @Test
    public void test_fetchAssetLocation() throws AssetManagerException {
        asset.setStatus("ACTIVE");
        when(assetRepository.findAssetByIdAndStatus(123456L, "ACTIVE")).thenReturn(asset);
        String assetLocation = assetService.fetchActiveAssetLocation(123456L);
        Assert.assertEquals("1234_file_1", assetLocation);
    }

    @Test(expected = AssetManagerException.class)
    public void test_invalidAssetId() throws AssetManagerException {
        when(assetRepository.findAssetByIdAndStatus(123456L,"ACTIVE")).thenReturn(null);
        String assetLocation = assetService.fetchActiveAssetLocation(123456L);
    }


}
