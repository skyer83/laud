/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package com.jfinal.club._admin.project;

import com.jfinal.club.common.model.Project;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.club.my.project.MyProjectService;
import com.jfinal.plugin.activerecord.Page;

/**
 * project 管理业务
 */
public class ProjectAdminService {

	public static final ProjectAdminService me = new ProjectAdminService();
	private Project dao = new Project().dao();

	/**
	 * 项目分页
	 */
	public Page<Project> paginate(int pageNum) {
		return dao.paginate(pageNum, 10, "select *", "from project order by id desc");
	}

	public Project edit(int id) {
		return dao.findById(id);
	}

	public Ret update(Project project) {
		project.update();
		return Ret.ok("msg", "修改成功");
	}

	/**
	 * 锁定
	 */
	public Ret lock(int id) {
		Db.update("update project set report = report + ? where id=?", Project.REPORT_BLOCK_NUM, id);
		return Ret.ok("msg", "锁定成功");
	}

	/**
	 * 解除锁定
	 */
	public Ret unlock(int id) {
		Db.update("update project set report = 0 where id=?", id);
		return Ret.ok("msg", "解除锁定成功");
	}

	/**
	 * 删除 project
	 */
	public Ret delete(int projectId) {
		Integer accountId = Db.queryInt("select accountId from project where id=? limit 1", projectId);
		if (accountId != null) {
			MyProjectService.me.delete(accountId, projectId);
			return Ret.ok("msg", "project 删除成功");
		} else {
			return Ret.ok("msg", "project 删除失败");
		}
	}
}
