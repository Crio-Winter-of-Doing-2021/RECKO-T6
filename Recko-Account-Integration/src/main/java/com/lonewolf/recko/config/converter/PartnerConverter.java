package com.lonewolf.recko.config.converter;

import com.lonewolf.recko.model.PartnerNameRepository;
import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;

public class PartnerConverter implements Converter<String, PartnerNameRepository> {

    @Override
    public PartnerNameRepository convert(@NonNull String source) {
        return PartnerNameRepository.parsePartner(source);
    }
}