package org.uniremington.shared.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data/**/@NoArgsConstructor/**/@AllArgsConstructor
public class BaseRequest<E> {
    private BaseHeader header;
    private E body;
}
