package org.uniremington.shared.base;

public class BaseResponse<E> {
    private BaseHeader header;
    private E body;
    private BaseError error;
}
