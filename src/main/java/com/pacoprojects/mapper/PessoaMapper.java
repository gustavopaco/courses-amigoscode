package com.pacoprojects.mapper;

import com.pacoprojects.auth.RegisterPessoaDto;
import com.pacoprojects.model.Pessoa;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PessoaMapper {

    Pessoa toEntity(RegisterPessoaDto registerPessoaDto);

    RegisterPessoaDto toDto(Pessoa pessoa);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Pessoa partialUpdate(RegisterPessoaDto registerPessoaDto, @MappingTarget Pessoa pessoa);
}
