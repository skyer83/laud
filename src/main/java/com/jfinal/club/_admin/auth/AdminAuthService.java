package com.jfinal.club._admin.auth;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;

/**
 * 后台授权业务
 */
public class AdminAuthService {

	public static final AdminAuthService me = new AdminAuthService();

	/**
	 * 是否为超级管理员，role.id 值为 1 的为超级管理员
	 */
	public boolean isSuperAdmin(int accountId) {
		SqlPara sp = Db.getSqlPara("admin.auth.isSuperAdmin", accountId);
		Integer ret = Db.queryInt(sp.getSql(), sp.getPara());
		return ret != null;
	}

	/**
	 * 是否拥有具体某个权限
	 */
	public boolean hasPermission(int accountId, String actionKey) {
		SqlPara sp = Db.getSqlPara("admin.auth.hasPermission", actionKey, accountId);
		Integer ret = Db.queryInt(sp.getSql(), sp.getPara());
		return ret != null;
	}
}

