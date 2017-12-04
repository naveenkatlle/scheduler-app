package com.ftd.autoexport.scheduler;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


public class CopyOfMySqlExportTask {
	
	private DataSource dataSource;
	public static int file_index = 1;
	public static ResultSet rs_previous;
	final static Logger logger = Logger.getLogger(CopyOfMySqlExportTask.class);
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void exportData() {
	
		logger.info("starting export");
		System.out.println("starting export");
		
		try {
            
	        Connection connection = dataSource.getConnection();
	        Statement st = connection.createStatement();
	        ResultSet rs = st.executeQuery("SELECT id,container,msg FROM activemq_msgs");
	
	        logger.info("executed query");
	        
	        
	        HSSFWorkbook wb = new HSSFWorkbook();
	        HSSFSheet sheet = wb.createSheet("Excel Sheet");
			HSSFRow rowhead = sheet.createRow((short) 0);
            rowhead.createCell((int) 0).setCellValue("ID");
            rowhead.createCell((int) 1).setCellValue("Container");
            rowhead.createCell((int) 2).setCellValue("Msg");
            int index = 1;
            logger.info("created worksbook and sheet");
	        
            
            while(rs.next()){
            	
            	HSSFRow row = sheet.createRow((int) index);
	            row.createCell((int) 0).setCellValue(rs.getInt(1));
	            row.createCell((int) 1).setCellValue(rs.getString(2));
	            row.createCell((int) 2).setCellValue(rs.getString(3));
	            
	            index++;
            }
            
            if(index != 1){
            
		    	String filePathString = "E:\\exportData\\exportFile_";
		    	logger.info("creating excel file excelFile_"+file_index + "_" + new Date().getTime() + ".xls");
	            FileOutputStream fileOut = new FileOutputStream(filePathString + file_index + "_" + new Date().getTime() + ".xls");
	            file_index++;
	            wb.write(fileOut);
	            fileOut.close();
	            logger.info("Data is saved in excel file.");  
            }  
            else{
            	logger.info("No Data to export!");
            }
            rs.close();
            connection.close();
            
            
	
		} catch (Exception e) {
			logger.error(e);
		}
		
		
	}
}