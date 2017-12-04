package com.ftd.autoexport.scheduler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class MySqlExportTask {

	private DataSource dataSource;
	public static int file_index = 1;
	public static final String filePath = "E:\\exportData\\exportFile_";
	final static Logger logger = Logger.getLogger(MySqlExportTask.class);

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public void exportData() {

		logger.info("starting export");
		System.out.println("starting export");
		String file = filePath + file_index++ + "_"
				+ new Date().getTime() + ".xls";

		try (Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT id,container,msg FROM activemq_msgs")) {

			logger.info("executed query");
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Excel Sheet");
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell((int) 0).setCellValue("ID");
			rowhead.createCell((int) 1).setCellValue("Container");
			rowhead.createCell((int) 2).setCellValue("Msg");
			int index = 1;
			logger.info("created worksbook and sheet");
			
			while (rs.next()) {
				HSSFRow row = sheet.createRow((int) index);
				row.createCell((int) 0).setCellValue(rs.getInt(1));
				row.createCell((int) 1).setCellValue(rs.getString(2));
				row.createCell((int) 2).setCellValue(rs.getString(3));
				index++;
			}
			if (index != 1) {
				logger.info("creating excel file: " + file);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				try {
				    wb.write(bos);
				} finally {
				    bos.close();
				}
				byte[] bytes = bos.toByteArray();
				Files.write(Paths.get(file), bytes);
				//way 1
				/*FileOutputStream fileOut = new FileOutputStream(file);
	            wb.write(fileOut);
	            fileOut.close();*/
				
				//way 2
				//Files.write(Paths.get(file), wb.getBytes());
				
				logger.info("Data is saved in excel file.");
				
			} else {
				logger.info("No Data to export!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}