package br.ufc.quixada.npi.gestaocompetencia.docs;

import org.springframework.http.HttpStatus;

import br.ufc.quixada.npi.gestaocompetencia.exception.ExceptionResponse;
import io.swagger.annotations.ApiModelProperty;

public class CustomResponses {
	
	public class AuthResponse{
		private String username;
		private String token;
		
		public String getUsername() {
			return username;
		}
		
		public String getToken() {
			return token;
		}
	}

	public class NotAlowedResponse extends ExceptionResponse{
		
		@ApiModelProperty(name="status", dataType = "HttpStatus.UNAUTHORIZED")
		@Override
		public void setStatus(HttpStatus status) {
			// TODO Auto-generated method stub
			super.setStatus(status);
		}
		
		@ApiModelProperty(example="HttpStatus.UNAUTHORIZED")
		@Override
		public HttpStatus getStatus() {
			// TODO Auto-generated method stub
			return super.getStatus();
		}
	}
	
	public class InternalServerError extends ExceptionResponse{		
		@ApiModelProperty(example="INTERNAL_SERVER_ERROR")
		@Override
		public HttpStatus getStatus() {
			return super.getStatus();
		}
		
		@ApiModelProperty(example="INTERNAL_SERVER_ERROR")
		@Override
		public String getError() {
			return super.getError();
		}
	}
}


