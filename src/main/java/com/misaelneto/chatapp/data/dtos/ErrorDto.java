package com.misaelneto.chatapp.data.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
	public Long timestamp;
	public Integer status;
	public String error;
	public String exception;
	public String message;
	public String path;
}