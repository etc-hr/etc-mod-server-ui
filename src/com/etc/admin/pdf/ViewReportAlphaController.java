package com.etc.admin.pdf;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.PageDrawer;
import org.apache.pdfbox.rendering.PageDrawerParameters;

import com.etc.admin.utils.Utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class ViewReportAlphaController {

	@FXML
	private ImageView imageView;
	
	/**
	 * initialize is called when the FXML is loaded
	 */
	@FXML
	public void initialize() {

	}
	
	private void createPDF() {
		try {
			// for documentation, how to grab a screenshot of the stage using new utils funciton:
			// Stage stage = (Stage) imageView.getScene().getWindow();
			// Utils.savePDFScreenshot(stage, null, true);
			
			
			// Create a new empty document
			PDDocument document = new PDDocument();
	
			// Create a new blank page and add it to the document
			//PDPage page = new PDPage();
			//document.addPage( page );
			
			// Create a new font object selecting one of the PDF base fonts
			PDFont font = PDType1Font.HELVETICA;
			
			/// start text
			final float fontSize = 10;
	        final float ascent = fontSize * font.getFontDescriptor().getAscent() / 1000;
	        final float descent = fontSize * -font.getFontDescriptor().getDescent() / 1000;
	        final float widthOfM = fontSize * font.getStringWidth("M") / 1000;
	        
	        final PDRectangle pageSize = PDRectangle.A4;
	        final PDPage page = new PDPage(pageSize);
	        document.addPage(page);
	        
	        final PDRectangle pageBox = page.getMediaBox();
	        final float width = pageBox.getWidth();
	        final float height = pageBox.getHeight();
	        
	        final float lineHeight = (ascent + descent) * 1.5f; // standard line height is 150% of font height
	        float startX = pageBox.getLowerLeftX();
	        float startY = pageBox.getUpperRightY();
	        
	        final PDPageContentStream contentStream = new PDPageContentStream(document, page);
	        contentStream.beginText();
	        contentStream.setFont(font, fontSize);
	        contentStream.newLineAtOffset(startX, startY - lineHeight + descent); // set position to baseline of text
	        contentStream.setLeading(lineHeight);
	        contentStream.showText("First Line");
	        contentStream.newLine();
	        contentStream.showText("Second Line");
	        contentStream.endText();			
			//// end

	        // Make sure that the content stream is closed:
			contentStream.close();
			// Save the newly created document
			document.save("c:\\temp\\testPage.pdf");
			
			// iterate through the pages
			//List<PDPage> pdPages = document.getDocumentCatalog().getAllPages();
			//document.getDocumentCatalog().getPages();
			
			//int page = 0;
			//for (PDPage pdPage : pdPages)
			//{ 
			//    ++page;
			//    BufferedImage bim = pdPage.convertToImage(BufferedImage.TYPE_INT_RGB, 300);
			//    ImageIOUtil.writeImage(bim, pdfFilename + "-" + page + ".png", 300);
			//}
			
			PDFRenderer pdfRenderer = new PDFRenderer(document);
			BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
			Image image = convertToFxImage(bim);
			imageView.setImage(image);

			// close it all 
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Image convertToFxImage(BufferedImage image) {
	    WritableImage wr = null;
	    if (image != null) {
	        wr = new WritableImage(image.getWidth(), image.getHeight());
	        PixelWriter pw = wr.getPixelWriter();
	        for (int x = 0; x < image.getWidth(); x++) {
	            for (int y = 0; y < image.getHeight(); y++) {
	                pw.setArgb(x, y, image.getRGB(x, y));
	            }
	        }
	    }

	    return new ImageView(wr).getImage();
	}
	
	@FXML
	private void onCreatePDF() {
		createPDF();
	}
	
	private void exitPopup() {
		Stage stage = (Stage) imageView.getScene().getWindow();
		stage.close();
	}
	
	@FXML
	private void onCancel(ActionEvent event) {
		exitPopup();
	}	
}


