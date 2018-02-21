package com.idega.block.sso.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AuthorizationSettings implements Serializable {

	private static final long serialVersionUID = -3195991815473541135L;

	private String type, remoteLoginService, remoteLoginEntityId, remoteLoginTarget, remoteLoginReturn, remoteLoginLogo, remoteLoginLabel;

	public String getRemoteLoginEntityId() {
		return remoteLoginEntityId;
	}

	public void setRemoteLoginEntityId(String remoteLoginEntityId) {
		this.remoteLoginEntityId = remoteLoginEntityId;
	}

	public String getRemoteLoginTarget() {
		return remoteLoginTarget;
	}

	public void setRemoteLoginTarget(String remoteLoginTarget) {
		this.remoteLoginTarget = remoteLoginTarget;
	}

	public String getRemoteLoginReturn() {
		return remoteLoginReturn;
	}

	public void setRemoteLoginReturn(String remoteLoginReturn) {
		this.remoteLoginReturn = remoteLoginReturn;
	}

	public String getRemoteLoginLogo() {
		return remoteLoginLogo;
	}

	public void setRemoteLoginLogo(String remoteLoginLogo) {
		this.remoteLoginLogo = remoteLoginLogo;
	}

	public String getRemoteLoginLabel() {
		return remoteLoginLabel;
	}

	public void setRemoteLoginLabel(String remoteLoginLabel) {
		this.remoteLoginLabel = remoteLoginLabel;
	}

	public String getRemoteLoginService() {
		return remoteLoginService;
	}

	public void setRemoteLoginService(String remoteLoginService) {
		this.remoteLoginService = remoteLoginService;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Auth. type: " + getType();
	}

}