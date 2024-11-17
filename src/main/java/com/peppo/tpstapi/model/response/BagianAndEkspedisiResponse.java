package com.peppo.tpstapi.model.response;

import com.peppo.tpstapi.entity.Bagian;
import com.peppo.tpstapi.entity.Ekspedisi;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BagianAndEkspedisiResponse {

    @NotEmpty
    private List<Bagian> bagianList;

    @NotEmpty
    private List<Ekspedisi> ekspedisiList;
}
