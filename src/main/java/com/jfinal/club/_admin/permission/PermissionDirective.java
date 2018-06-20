package com.jfinal.club._admin.permission;

import com.jfinal.club._admin.auth.AdminAuthInterceptor;
import com.jfinal.club._admin.auth.AdminAuthService;
import com.jfinal.club.common.model.Account;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 界面上的权限控制功能
 * 用来控制界面上的菜单、按钮等等元素的显示
 *
 * 使用示例见模板文件： /_view/_admin/project/index.html 或者 /_view/_admin/permission/index.html
 * #permission("/admin/project/edit")
 * 		<a href="/admin/project/edit?id=#(x.id)">
 * 	  	  <i class="fa fa-pencil" title="修改"></i>
 * 		</a>
 * #end
 *
 * 别名： #perm() #end
 */
public class PermissionDirective extends Directive {

	public void exec(Env env, Scope scope, Writer writer) {
		Account account = AdminAuthInterceptor.getThreadLocalAccount();
		if (account != null && account.isStatusOk()) {
			// 如果是超级管理员，或者拥有指定的权限则放行
			if (AdminAuthService.me.isSuperAdmin(account.getId())) {
				stat.exec(env, scope, writer);
			} else {
				Object value = exprList.eval(scope);
				if (!(value instanceof String)) {
					throw new IllegalArgumentException("权限参数只能为 String 类型");
				}

				if (hasPermission(account.getId(), (String)value)) {
					stat.exec(env, scope, writer);
				}
			}
		}
	}

	/**
	 * 当前账号是否拥有某个权限
	 */
	public static boolean hasPermission(int accountId, String actionKey) {
		if (StrKit.isBlank(actionKey)) {
			return false;
		}

		Kv data = Kv.by("accountId", accountId).set("actionKey", actionKey);
		SqlPara sp = Db.getSqlPara("admin.permission.hasPermission", data);
		Integer ret = Db.queryInt(sp.getSql(), sp.getPara());
		return ret != null;
	}

	public boolean hasEnd() {
		return true;
	}
}