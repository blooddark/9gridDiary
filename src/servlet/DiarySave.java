package servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.DiaryDAO;
import sun.misc.BASE64Decoder;

public class DiarySave extends HttpServlet {
	private static final long serialVersionUID = -5815196502656815479L;

	public DiarySave() {
		super();
	}

	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String title = request.getParameter("title");
		int userid = (int) request.getSession().getAttribute("id");
		String username = (String) request.getSession().getAttribute("username");
		new DiaryDAO().InsertDiary(title, userid, username);
		int articleid = new DiaryDAO().SelectDiary(userid).get(0).getId();
		GenerateImage(request.getParameter("pic"), String.valueOf(articleid),
				getServletConfig().getServletContext().getRealPath("/") + "images");
		response.sendRedirect("Diary?Page=1&from=mine");
	}

	public static boolean GenerateImage(String imgStr, String imgName, String imgPath) {
		// 对Base64字符串解码并生成图片
		if (imgStr == null) {
			return false;
		} // 图像数据为空
		imgStr = imgStr.substring(22);
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgStr);
			/*
			 * for (int i = 0; i < b.length; ++i) { if (b[i] < 0) { b[i] += 256;
			 * } // 调整异常数据 }
			 */
			File headPath = new File(imgPath);// 获取文件夹路径
			if (!headPath.exists()) {// 判断文件夹是否创建，没有创建则创建新文件夹
				headPath.mkdirs();
			}
			// 定义图片路径
			String imgFilePath = imgPath + "/" + imgName + ".jpg";
			// 新生成的图片
			OutputStream out = new FileOutputStream(imgFilePath);
			out.write(b);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void init() throws ServletException {
	}

}
