package in.apt.assetmanager.utils;

import in.apt.assetmanager.entity.Asset;
import in.apt.assetmanager.models.AssetModel;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectMapper {

    private static ModelMapper modelMapper;

    static {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).getConverters();
        Converter<Date, String> dateToMillis =
                ctx -> Long.toString(ctx.getSource().getTime());
        PropertyMap<Asset, AssetModel> dateMap = new PropertyMap<Asset, AssetModel>(){

            @Override
            protected void configure() {
                using(dateToMillis).map(source.getTimestamp(), destination.getTimestamp());
            }
        };
        modelMapper.addMappings(dateMap);
    }

    private ObjectMapper() {}

    public static <D, T> D map(T entity, Class<D> outClass){
        return modelMapper.map(entity, outClass);
    }

    public static <D, T> List<D> mapAll(Collection<T> entityList, Class<D> outCLass) {
        return entityList.stream()
                .map(entity -> map(entity, outCLass))
                .collect(Collectors.toList());
    }

    public static <S, D> D map(S source, D destination) {
        modelMapper.map(source, destination);
        return destination;
    }
}
