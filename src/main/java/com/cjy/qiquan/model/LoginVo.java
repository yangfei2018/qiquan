package com.cjy.qiquan.model;

public class LoginVo<T> {

	private String token;

	private T user;
	

	public LoginVo(Builder<T> b) {
		this.token = b.token;
		this.user = b.user;
	}

	public static class Builder<T> {

		private String token;
		private T user;
		
		public Builder() {

		}

		public Builder<T> setToken(final String token) {
			this.token = token;
			return this;
		}

		public Builder<T> setPartner(final T user) {
			this.user = user;
			return this;
		}

		public LoginVo<T> build() {
			return new LoginVo<T>(this);
		}

		
	}

	public String getToken() {
		return token;
	}

	public T getUser() {
		return user;
	}
	
}
