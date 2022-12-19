package ch09;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

/**
 * Servlet implementation class StudentController
 */


@WebServlet("/studentControl")
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	StudentDAO dao;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);  //서블릿 초기하
		dao = new StudentDAO();  //只创建一次StudentDAO ->可以共享使用
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		//request: 뷰에서 넘어온 데이터, 정보가 들어있다.
		String action = request.getParameter("action");  //insert.
		String view = "";
		
		if(action == null) {
			getServletContext().getRequestDispatcher("/studentControl?action=list")
			.forward(request, response);  //不跳转画面，改变了url
			
		}else{
			switch(action) {
				case "list": view = list(request, response);
				break;
				case "insert": view = insert(request, response); //request, response 객체를 매개변수로 넘겨준다.
				break;
			}
			//getServletContext(): ServletContext를 얻어움
		//getRequestDispatcher(이동할 페이지)
		//forward:페이지를 이동시키다. 내부에서 이동이 되므로 주소가 변하지 안는다.
		//getServletContext().getRequestDispatcher("/ch09/studentInfo.jsp");
		getServletContext().getRequestDispatcher("/ch09/" + view)
		.forward(request, response);
		}
		
	} 
	
	public String list(HttpServletRequest request, HttpServletResponse response) {
		//request.setAttribute("key", object );
		request.setAttribute("students", dao.getAll()); //request, response할때 값을 넘겨준다.
		return "studentInfo.jsp";
	}
	
	
	//requst 데이터 받아옴 -> DAO에 있는 insert 실행(DB에 insert가됨) -> 페이지명(studentInfo.jsp)리턴
	public String insert(HttpServletRequest request, HttpServletResponse response) {
		Student s = new Student();
			try {
				BeanUtils.populate(s, request.getParameterMap());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//   	BeanUtils.populate(s, request.getParameterMap()); ->이래 코드의 열할을 대신 해준다.
//		s.setUsername(request.getParameter("username"));
//		s.setEmail(request.getParameter("email"));
//		s.setUniv(request.getParameter("univ"));
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 mm월 dd일");
//		Date date  = (Date)formatter.parse(request.getParameter("birth"));
//		s.setBirth(request.getParameter("birth"));
		
		
		
		dao.insert(s); //컨트롤러는 DAO한테 있는 메소드를 사용한다. DAO한테 데이터베이스관
		return "studentInfo.jsp";
		
	}
       
    
}
