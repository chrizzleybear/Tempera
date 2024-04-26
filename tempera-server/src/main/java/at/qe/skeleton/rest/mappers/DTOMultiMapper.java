package at.qe.skeleton.rest.mappers;

import java.util.List;

public interface DTOMultiMapper <e,d>{
    d mapToDto(e temperature, e irradiance, e humidity, e nmvoc) throws Exception;
    List<e> mapFromDto(d dto) throws Exception;
}
