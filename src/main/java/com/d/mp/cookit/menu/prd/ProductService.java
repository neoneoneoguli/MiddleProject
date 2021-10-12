package com.d.mp.cookit.menu.prd;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.d.mp.util.FileManagerProduct;

@Service
public class ProductService {
	
	@Autowired
	private ProductDAO productDAO;
	@Autowired
	private ServletContext servletContext;
	@Autowired
	private FileManagerProduct fileManager;
	
	
	/* ================================================= 상품등록 ================================================= */
	
	
	public int setInsert(ProductDTO productDTO, List<MultipartFile> main_files, List<MultipartFile> slider_files) throws Exception{
		
		
		int result = productDAO.setInsert(productDTO);
		
		///////////////// start end 사이 날짜 구하기 /////////////////////////
		
		String s_date = productDTO.getProduct_start_date();
		String e_date = productDTO.getProduct_end_date();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d1 = df.parse(s_date);
		Date d2 = df.parse(e_date);
		
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		
		c1.setTime(d1);
		c2.setTime(d2);
		
		while(c1.compareTo(c2) != 1) {
			System.out.println(df.format(c1.getTime()));
			
			productDTO.setProduct_regdate(df.format(c1.getTime()));
			productDAO.setInsertDate(productDTO);
			
			c1.add(Calendar.DATE, 1);
		}
		
		
		///////////////// 파일 처리 /////////////////////////
		System.out.println(productDTO.getProduct_id());
		
		String main_realPath = servletContext.getRealPath("/resources/upload/menu/main/" + productDTO.getProduct_id());
		String slider_realPath = servletContext.getRealPath("/resources/upload/menu/slider/" + productDTO.getProduct_id());
	
		File main_file_path = null;
		File slider_file_path = null;
		
		
		/* MultiPartFiles 버그성 아무것도 넣지 않아도 input에서 하나가 넘어오는걸로 되가지고 size 1 임 */
		if(main_files.size() < 2 && slider_files.size() < 2) {
			System.out.println("첨부된 파일이 없습니다.");
		}
		if(main_files.size() >= 1){
			main_file_path = new File(main_realPath);
			
			for(MultipartFile multipartFile:main_files) {
				String fileName = fileManager.fileSave(main_file_path, multipartFile);
				ProductFilesDTO productFilesDTO = new ProductFilesDTO();
				productFilesDTO.setProduct_file_name(fileName);
				productFilesDTO.setProduct_file_ori_name(multipartFile.getOriginalFilename());
				productFilesDTO.setProduct_id(productDTO.getProduct_id());
				productFilesDTO.setProduct_file_path("main");
				
				result = productDAO.setFile(productFilesDTO);
			}
		}
		if(slider_files.size() >= 2){
			slider_file_path = new File(slider_realPath);
			
			for(MultipartFile multipartFile:slider_files) {
				String fileName = fileManager.fileSave(slider_file_path, multipartFile);
				ProductFilesDTO productFilesDTO = new ProductFilesDTO();
				productFilesDTO.setProduct_file_name(fileName);
				productFilesDTO.setProduct_file_ori_name(multipartFile.getOriginalFilename());
				productFilesDTO.setProduct_id(productDTO.getProduct_id());
				productFilesDTO.setProduct_file_path("slider");
				
				result = productDAO.setFile(productFilesDTO);
			}
		}
		
		
		//////////////////////////////////////////////////////////////////////////////////////////////////
		
		return result;
	}
	
	/* ================================================= 상품등록 끝 ================================================= */
	
	
	
	
	// 등록된 상품들의 파일 가져오기
	public List<ProductFilesDTO> getFile(ProductDTO productDTO) throws Exception{
		return productDAO.getFile(productDTO);
	}
	
	// 등록된 상품들 가져오기
	public List<ProductDTO> getPrdList(ProductDTO productDTO) throws Exception{
		
		if(productDTO.getDate() != null) {
			SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
			
			SimpleDateFormat afterFromat = new SimpleDateFormat("yyyy-mm-dd");
			
			Date tempDate = null;
			
			tempDate = beforeFormat.parse(productDTO.getDate());
			
			String transDate = afterFromat.format(tempDate);
			
			productDTO.setDate(transDate);
		}
		
		return productDAO.getPrdList(productDTO);
	}
	
	// 특정 id의 상품 하나 가지고 오기
	public ProductDTO getPrdOne(ProductDTO productDTO) throws Exception{
		
		return productDAO.getPrdOne(productDTO);
	}
	
	// 특정 id 상품의 주문가능 일자 가져오기
	public List<ProductDTO> getDate(ProductDTO productDTO) throws Exception{
		
		return productDAO.getDate(productDTO);
	}
	
	// 특정 id의 상품 하나 삭제하기
	public int deletePrdOne(ProductDTO productDTO) throws Exception{
		
		return productDAO.deletePrdOne(productDTO);
	}
}
