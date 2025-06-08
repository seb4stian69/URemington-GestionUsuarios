package org.uniremington.shared.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data/**/@NoArgsConstructor/**/@AllArgsConstructor
public class BaseResponse<E> {

    private BaseHeader header;
    private E body;
    private BaseError error;

    public BaseResponse(BaseHeader header, BaseError error) {
        this.header = header;
        this.error = error;
    }
}
