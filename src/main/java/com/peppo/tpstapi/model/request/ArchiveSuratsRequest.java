package com.peppo.tpstapi.model.request;

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
public class ArchiveSuratsRequest {

    @NotEmpty
    private List<Integer> listIdSurat;

}
