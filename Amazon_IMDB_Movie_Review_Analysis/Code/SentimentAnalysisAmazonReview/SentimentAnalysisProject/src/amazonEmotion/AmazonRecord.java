package amazonEmotion;


import org.apache.hadoop.io.Text;

public class AmazonRecord {

	//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	String product_productId;
	String review_userId;
	String review_profileName;
	String review_helpfulness;
	double review_score;
	int review_time;
	String review_summary;
	String review_text;

	public AmazonRecord(String inputString) throws IllegalArgumentException {
		
		String[] attributes = splitingInput(inputString);
		System.out.println("inputString..........."+inputString);
		if (attributes.length != 8){
			//System.out.println("length"+attributes.length );
			throw new IllegalArgumentException(String.format("Input string given did not have 8 values in CSV format, contained %i values", attributes.length));
		}
		try {
			setProduct_productId(attributes[0]);
			setReview_userId(attributes[1]);
			setReview_profileName(attributes[2]);
			setReview_helpfulness(attributes[3]);
			//System.out.println("score setting.."+attributes[4]);
			
			setReview_score(Double.parseDouble(attributes[4]));
			setReview_time(Integer.parseInt(attributes[5]));
			setReview_summary(attributes[6]);
			setReview_text(attributes[7]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}
	
	public AmazonRecord getAmazonRecordEntry(String inputString, AmazonRecord amaRecord) {
			
			String[] attributes = splitingInput(inputString);
			//System.out.println("inputString..........."+inputString);
			if (attributes.length != 8)
				throw new IllegalArgumentException(String.format("Input string given did not have 8 values in CSV format, contained %i values", attributes.length));
	
			try {
				amaRecord.setProduct_productId(attributes[0]);
				amaRecord.setReview_userId(attributes[1]);
				amaRecord.setReview_profileName(attributes[2]);
				amaRecord.setReview_helpfulness(attributes[3]);
				//.out.println("score setting.."+attributes[4]);
				
				amaRecord.setReview_score(Double.parseDouble(attributes[4]));
				amaRecord.setReview_time(Integer.parseInt(attributes[5]));
				amaRecord.setReview_summary(attributes[6]);
				amaRecord.setReview_text(attributes[7]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			return amaRecord; 
		}
	
	public String[] splitingInput(String line){
		String [] word= new String[8];
		int count=0;
		
		String lineContent[]= line.split(";;;");
		//System.out.println();
		if(lineContent.length==8){
			if(line.toLowerCase().contains("product/productId".toLowerCase())){
				String[] contentsOfString = lineContent[0].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=(null);
				count++;
			}
			if(line.toLowerCase().contains("review/userId".toLowerCase())){
				String[] contentsOfString = lineContent[1].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=(null);
				count++;
			}
			if(line.toLowerCase().contains("review/profileName".toLowerCase())){
				String[] contentsOfString = lineContent[2].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=(null);
				count++;
			}if(line.toLowerCase().contains("review/helpfulness".toLowerCase())){
				String[] contentsOfString = lineContent[3].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=(null);
				count++;
			}if(line.toLowerCase().contains("review/score".toLowerCase())){
				String[] contentsOfString = lineContent[4].split(":");
				word[count]=(contentsOfString[1].trim());
				System.out.println("score"+ word[count]);
				count++;
			}else{
				word[count]="0";
				count++;
			}if(line.toLowerCase().contains("review/time".toLowerCase())){
				String[] contentsOfString = lineContent[5].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=("0");
				count++;
			}if(line.toLowerCase().contains("review/summary".toLowerCase())){
				String[] contentsOfString = lineContent[6].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=(null);
				count++;
			}if(line.toLowerCase().contains("review/text".toLowerCase())){
				String[] contentsOfString = lineContent[7].split(":");
				word[count]=(contentsOfString[1].trim());
				count++;
			}else{
				word[count]=(null);
				count++;
			}
			return word;
		}
		return lineContent;
		 
		
	}

	public AmazonRecord(Text inputText) throws IllegalArgumentException {
		this(inputText.toString());
	}

	public String getProduct_productId() {
		return product_productId;
	}

	public void setProduct_productId(String product_productId) {
		this.product_productId = product_productId;
	}

	public String getReview_userId() {
		return review_userId;
	}

	public void setReview_userId(String review_userId) {
		this.review_userId = review_userId;
	}

	public String getReview_profileName() {
		return review_profileName;
	}

	public void setReview_profileName(String review_profileName) {
		this.review_profileName = review_profileName;
	}

	public String getReview_helpfulness() {
		return review_helpfulness;
	}

	public void setReview_helpfulness(String review_helpfulness) {
		this.review_helpfulness = review_helpfulness;
	}

	public double getReview_score() {
		return review_score;
	}

	public void setReview_score(double review_score) {
		this.review_score = review_score;
	}

	public int getReview_time() {
		return review_time;
	}

	public void setReview_time(int review_time) {
		this.review_time = review_time;
	}

	public String getReview_summary() {
		return review_summary;
	}

	public void setReview_summary(String review_summary) {
		this.review_summary = review_summary;
	}

	public String getReview_text() {
		return review_text;
	}

	public void setReview_text(String review_text) {
		this.review_text = review_text;
	}

	
	
}
