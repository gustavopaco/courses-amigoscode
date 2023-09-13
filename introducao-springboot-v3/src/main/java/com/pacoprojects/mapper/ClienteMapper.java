package com.pacoprojects.mapper;

import com.pacoprojects.dto.ClienteDto;
import com.pacoprojects.dto.ClienteDtoList;
import com.pacoprojects.model.Cliente;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClienteMapper {
    Cliente toEntity(ClienteDto clienteDto);

    ClienteDto toDto(Cliente cliente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Cliente partialUpdate(ClienteDto clienteDto, @MappingTarget Cliente cliente);

    Cliente toEntity1(ClienteDtoList clienteDtoList);

    ClienteDtoList toDto1(Cliente cliente);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Cliente partialUpdate1(ClienteDtoList clienteDtoList, @MappingTarget Cliente cliente);
}
