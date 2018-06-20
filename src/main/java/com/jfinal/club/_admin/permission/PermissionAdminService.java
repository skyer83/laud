package com.jfinal.club._admin.permission;

import com.jfinal.club.common.controller.BaseController;
import com.jfinal.club.common.model.Permission;
import com.jfinal.core.Action;
import com.jfinal.core.JFinal;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 权限管理业务
 */
public class PermissionAdminService {

	public static final PermissionAdminService me = new PermissionAdminService();
	private Permission dao = new Permission().dao();

	// 用于排除掉 BaseController 中的几个成为了 action 的方法
	private Set<String> excludedMethodName = buildExcludedMethodName();

	private Set<String> buildExcludedMethodName() {
		Set<String> excludedMethodName = new HashSet<String>();
		Method[] methods = BaseController.class.getMethods();
		for (Method m : methods) {
			if (m.getParameterTypes().length == 0)
				excludedMethodName.add(m.getName());
		}
		return excludedMethodName;
	}

	public Page<Permission> paginate(int pageNum) {
		return dao.paginate(pageNum, 10, "select *", "from permission order by actionKey asc");
	}

	/**
	 * 替换控制器前缀，界面显示时更加美观
	 *
	 * 例子：
	 * replaceControllerPrefix(permissionPage, "com.jfinal.club._admin.", "...");
	 * 以上例子将 "com.jfinal.club._admin." 这一长串前缀替换成 "..."，显示更美观
	 */
	public void replaceControllerPrefix(Page<Permission> permissionPage, String replaceTarget, String replacement) {
		for (Permission p : permissionPage.getList()) {
			String c = p.getController().replace(replaceTarget, replacement);
			p.setController(c);
		}
	}

	/**
	 * 同步 permission
	 * 获取后台管理所有 actionKey 以及 controller，将数据自动写入 permission 表
	 * 随着开发过程的前行，可以动态进行同步添加新的 permission 数据
	 */
	public Ret sync() {
		int counter = 0;
		List<String> allActionKeys = JFinal.me().getAllActionKeys();
		for (String actionKey : allActionKeys) {
			// 只处理后台管理 action，其它跳过
			if (actionKey.startsWith("/admin")) {
				String[] urlPara = new String[1];
				Action action = JFinal.me().getAction(actionKey, urlPara);
				if (action == null || excludedMethodName.contains(action.getMethodName())) {
					continue ;
				}

				String controller = action.getControllerClass().getName();

				String sql = "select * from permission where actionKey=? and controller = ? limit 1";
				Permission permission = dao.findFirst(sql, actionKey, controller);

				if (permission == null) {
					permission = new Permission();
					permission.setActionKey(actionKey);
					permission.setController(controller);
					permission.save();
					counter++;
				}
			}
		}

		if (counter == 0) {
			return Ret.ok("msg", "权限已经是最新状态，无需更新");
		} else {
			return Ret.ok("msg", "权限更新成功，共更新权限数 : " + counter);
		}
	}

	public Permission findById(int id) {
		return dao.findById(id);
	}

	public List<Permission> getAllPermissions() {
		return dao.find("select * from permission order by controller asc");
	}

	public Ret update(Permission permission) {
		permission.keep("id", "remark");	// 暂时只允许更新 remark
		permission.update();
		return Ret.ok("msg", "更新成功");
	}

	/**
	 * 如果某个 action 已经删掉，或者改了名称，可以使用该方法删除
	 */
	public Ret delete(final int permissionId) {
		Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				Db.delete("delete from role_permission where permissionId = ?", permissionId);
				dao.deleteById(permissionId);
				return true;
			  }
		});

		return Ret.ok("msg", "权限删除成功");
	}
}