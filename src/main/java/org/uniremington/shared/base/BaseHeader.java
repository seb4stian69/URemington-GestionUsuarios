package org.uniremington.shared.base;

import lombok.*;

@Getter@Setter/**/@AllArgsConstructor@NoArgsConstructor
public class BaseHeader {
    private String ip;
    private String usuario;
    private String authToken;
    private String llaveSimetrica;
}
