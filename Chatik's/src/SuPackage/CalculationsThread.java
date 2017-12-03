package SuPackage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

//import com.itextpdf.text.Anchor;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Font;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.Paragraph;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.pdf.BaseFont;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;

public class CalculationsThread implements Runnable {
	private Thread t;
	private LinkedList<Statistic> statisticData;

	public CalculationsThread() {
		statisticData = new LinkedList<>();
		t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (true) {
			Statistic statisticDataHour = new Statistic();
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			statisticDataHour.setTime(formatter.format(new Date(System.currentTimeMillis())));
			statisticDataHour.setNewConnectedClientCount(Server.getClientCount());
			Server.setClientCount(0);
			statisticDataHour.setMessageCount(Server.getMessageCount());
			Server.setMessageCount(0);
			statisticDataHour.setClientsOnline(Server.getUsersOnline().size());
			if (statisticData.size() >= 50) {
				statisticData.removeFirst();
			}
			statisticData.addLast(statisticDataHour);
//			for (int i = 0; i < statisticData.size(); i++) {
//				System.out.println(statisticData.get(i).toString());
//			}
//			System.out.println("__________________________");
//			Document document = new Document(PageSize.A4, 50, 50, 50, 50);
//			final String FONT = "FreeSans.ttf";
//			BaseFont bf;
//			try {
//				bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//				Font f = new Font(bf, 10);
//				formatter = new SimpleDateFormat("dd.MM.yyyy");
//				Anchor anchorTarget = new Anchor("Звіт про роботу програми згенерований " +formatter.format(new Date(System.currentTimeMillis())), f);
//				anchorTarget.setName("Top");
//				Paragraph paragraph1 = new Paragraph();
//				paragraph1.setSpacingBefore(50);
//				paragraph1.add(anchorTarget);
//				PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("D:\\Report.pdf"));
//				document.open();
//				document.add(paragraph1);
//				PdfPTable t = new PdfPTable(4);
//				t.setSpacingBefore(20);
//				t.setSpacingAfter(20);
//				PdfPCell c = new PdfPCell(
//						new Phrase("Час",f));
//				t.addCell(c);
//				c = new PdfPCell(
//						new Phrase("Користувачі онлайн",f));
//				t.addCell(c);
//				c = new PdfPCell(new Phrase("Нові користувачі",f));
//				t.addCell(c);
//				c = new PdfPCell(new Phrase("Кількість переданих повідомлень",f));
//				t.addCell(c);
//				formatter = new SimpleDateFormat("HH:mm:ss");
//				for (int i = 0; i < statisticData.size(); i++) {
//					c = new PdfPCell(
//							new Phrase(statisticData.get(i).getTime(), f));
//					t.addCell(c);
//					c = new PdfPCell(
//							new Phrase(new Integer(statisticData.get(i).getClientsOnline()).toString(), f));
//					t.addCell(c);
//					c = new PdfPCell(
//							new Phrase(new Integer(statisticData.get(i).getNewConnectedClientCount()).toString(), f));
//					t.addCell(c);
//					c = new PdfPCell(
//							new Phrase(new Integer(statisticData.get(i).getMessageCount()).toString(), f));
//					t.addCell(c);
//				}
//				document.add(t);
//				boolean haslegend = true;
//				Graphics d = new Graphi();
//				BufferedImage bimage = new BufferedImage(3000, 2000, BufferedImage.TYPE_INT_ARGB);
//				Graphics2D g = bimage.createGraphics();
//				g.setColor(Color.white);
//				g.fillRect(0, 0, 100, 100);
//				ByteArrayOutputStream out = new ByteArrayOutputStream();
//				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//				JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
//				param.setQuality(1.0f, true);
//				encoder.encode(bimage, param);
//				out.close();
				
//				document.close();
//			} catch (DocumentException | IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public LinkedList<Statistic> getStatisticData() {
		return statisticData;
	}

	public void setStatisticData(LinkedList<Statistic> statisticData) {
		this.statisticData = statisticData;
	}

}
