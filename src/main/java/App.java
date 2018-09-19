import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.Document;


public class App extends TelegramLongPollingBot {
	
    private static final String botUserName = "@Vodokanal_CK";
    private static final String token = "628535430:AAHgkSp6HMqgw3ILsCMS8kQjzZZiZqfuJt4";
	// в процессе удаления				
    String user_first_name2;
    String user_last_name2;
    static String lich_value3;
    static String lich_num;
    static String name_of_lich;
    String mes1;
    String mes2;

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new App());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
        System.out.println("Привіт, я у мережі!");
    }
    
    //добавляем к-во лич в базу
    private int add_lich(Integer user_id, Integer lich) {
    	
        //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
        @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_voda");
        MongoCollection<Document> collection = database.getCollection("users");
        //заменяем существующее значение рахунка на вводимый
        collection.updateOne(Filters.eq("id", user_id), new Document("$set", new Document("kvo", lich)));

    return lich;	
    }

//    private Document rename_lich(Integer user_id, String data_text, String answer_lich_name, Long chat_id, String lich_count_num, Integer year, Integer month, Integer date) {    	
//        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
//        @SuppressWarnings("resource")
//		MongoClient mongoClient = new MongoClient(connectionString);
//        MongoDatabase database = mongoClient.getDatabase("db_voda");
//        MongoCollection<Document> collection = database.getCollection("users");
////        Document doc = new Document("lich_name", answer_lich_name)
////        		.append(year.toString(), 
////        			new Document(month.toString(),
////        				new Document(date.toString(),
////        					new Document("value", ""))));
//        Document doc = new Document("lich_name", answer_lich_name);
//        collection.updateOne(new Document("id", user_id), Updates.set(lich_count_num, doc));
//        mongoClient.close();
//    return doc;
//    }
    
    private String save_value_lich (Integer user_id, String lich_num, String lich_value2, String value_date, String lich_name, boolean n_or_p ) {
    	System.out.println("save/reneme : "+user_id+" - "+lich_num+" - "+lich_value2+" - "+value_date+" - "+lich_name+" - "+n_or_p);
        //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
    	MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
        @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_voda");
        MongoCollection<Document> collection = database.getCollection("users");
        //rename lich
        Document fn = collection.find(eq("id", user_id)).first();
        Object value1 = fn.get("lich"+lich_num, new Document("lich_value", new Document("value","")));// .getString("lich"+lich_num);
        Object value2 = ((Document) value1).get("lich_value", new Document("value",""));// .getString("lich"+lich_num);
        Object value3 = ((Document) value2).get("value","");// .getString("lich"+lich_num);
        String value_old = value3.toString();

        System.out.println("find11 "+value3);
        System.out.println("find22 "+value_old);
        if (n_or_p == true) {

        	DB db = mongoClient.getDB("db_voda");
        	DBCollection coll = db.getCollection("users");
        	BasicDBObject searchQuery = new BasicDBObject("id", user_id);//.append("lich1.lichname", "641400902545024");
        	DBCursor cursor = coll.find(searchQuery);
        	while(cursor.hasNext()) {
        		DBObject currDocument = cursor.next();
    	    		try {
    	    			BasicDBObject newDocument = new BasicDBObject("lich"+lich_num, currDocument.get("lich"+lich_num));
    	    			if ((DBObject)newDocument.get("lich"+lich_num) == null ) {
    	    				System.out.println("лічильник не переименован и нету в базе 000");	
            	    		//lich_name = "Лічильник"+lich_num;
            	    		//lich_name = ((DBObject)newDocument.get("lich"+lich_num)).get("lich_name").toString();
            	    		
    	    				Document doc = new Document("lich_name",lich_name).append("lich_value", new Document("value", value_old).append("date", value_date));
    	    				collection.updateOne(new Document("id", user_id), Updates.set("lich"+lich_num, doc));
    	    			}
    	    			else {
    	    				Document doc = new Document("lich_name",lich_name).append("lich_value", new Document("value", value_old).append("date", value_date));
    	    				collection.updateOne(new Document("id", user_id), Updates.set("lich"+lich_num, doc));
    	    			}
    	    		}
    	    		finally {}
        	} cursor.close(); mongoClient.close();
        }
       //vvod_pokaznukiB
        else {
    		DB db = mongoClient.getDB("db_voda");
        	DBCollection coll = db.getCollection("users");
        	BasicDBObject searchQuery = new BasicDBObject("id", user_id);//.append("lich1.lichname", "641400902545024");
        	DBCursor cursor = coll.find(searchQuery);
         	while(cursor.hasNext()) {
        	    DBObject currDocument = cursor.next();
        	    	try {
        	    		BasicDBObject newDocument = new BasicDBObject("lich"+lich_num, currDocument.get("lich"+lich_num));
        	    		if ((DBObject)newDocument.get("lich"+lich_num) == null ) {
        	    			System.out.println("лічильник не переименован и нету в базе 000");	
            	    		lich_name = "Лічильник"+lich_num;
            	    		
            	    		Document doc = new Document("lich_name",lich_name).append("lich_value", new Document("value", lich_value2).append("date", value_date));
            	            collection.updateOne(new Document("id", user_id), Updates.set("lich"+lich_num, doc));
        	    		}
        	    		else {
        	    		lich_name = ((DBObject)newDocument.get("lich"+lich_num)).get("lich_name").toString();
        	    		
        	    		Document doc = new Document("lich_name",lich_name).append("lich_value", new Document("value", lich_value2).append("date", value_date));
        	            collection.updateOne(new Document("id", user_id), Updates.set("lich"+lich_num, doc));
        	    		}
        	    	}
        	    	finally {}
        	}  
         	cursor.close(); mongoClient.close();	 
       } 
    return null;	
    }

	//добавляем номер счета в БД
    private int add_pax(Integer user_id, Integer answer_pax, Long chat_id) {
    	
        //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
    	MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
        @SuppressWarnings("resource")
		MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_voda");
        MongoCollection<Document> collection = database.getCollection("users");
        //заменяем существующее значение рахунка на вводимый
        collection.updateOne(Filters.eq("id", user_id), new Document("$set", new Document("pax", answer_pax)));
        mongoClient.close();
    return answer_pax;	
    }
    
    // видалення рахунку	
	private void del(Integer user_id) {
	    //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
	    MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
	    @SuppressWarnings("resource")
	    MongoClient mongoClient = new MongoClient(connectionString);
	    MongoDatabase database = mongoClient.getDatabase("db_voda");
	    MongoCollection<Document> collection = database.getCollection("users");
	    //заменяем существующее значение рахунка на вводимый
	    collection.updateOne(Filters.eq("id", user_id), new Document("$set", new Document("pax", null)));
	    return ;	
	  }
	  
    private static  Document info(Integer user_id) {
      //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
      MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
      MongoClient mongoClient = new MongoClient(connectionString);
      MongoDatabase database = mongoClient.getDatabase("db_voda");
      MongoCollection<Document> collection = database.getCollection("users");
      System.out.println("Принял и ищу user_id по базе."+user_id);
      Document fn = collection.find(eq("id", user_id)).first();
      mongoClient.close();
      System.out.println("Отсылаяю найденный документ."+fn);
      return fn;
}

    
    // Проверка на сущ. пользователя в БД, если нет = добавляет! 
    //Запускается один раз при нажатии /Старт
	  @SuppressWarnings("deprecation")
	private Document check(Integer user_id, Update update) {

    	String first_name = update.getMessage().getFrom().getFirstName();
    	String last_name = update.getMessage().getFrom().getLastName();
    	String username = update.getMessage().getFrom().getUserName();

        //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");//?ssl=true
        MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
        //"mongodb+srv://kay:myRealPassword@cluster0.mongodb.net/");
//      SSLContext sslContext = ...
//		MongoClientOptions options = MongoClientOptions.builder()
//														.compressorList(Arrays.asList(MongoCompressor.createSnappyCompressor(), 
//																					  MongoCompressor.createZlibCompressor()))
//		                                                 .sslEnabled(true)  //.sslInvalidHostNameAllowed(true) отключение хоста
//		                                                 .sslContext(sslContext)
//		                                                 .build();
//		MongoClient mongoClient = new MongoClient(connectionString, options);
        MongoClient mongoClient = new MongoClient(connectionString);
        MongoDatabase database = mongoClient.getDatabase("db_voda");
        MongoCollection<Document> collection = database.getCollection("users");

        //Получить список коллекций
//        for (String name : database.listCollectionNames()) {
//            System.out.println(name); // =users список подбаз(колекций)
//        }

        long found = collection.count(Document.parse("{id : " + (user_id) + "}"));
        if (found == 0) {
			Document doc = new Document("first_name", first_name)
                    .append("last_name", last_name)
                    .append("id", user_id)
                    .append("kvo", 0)
                    .append("username", username)
            		.append("pax", 0);
			//languageCode='uk' - дописать и потом сделать смену языка!
            collection.insertOne(doc);
            Document fn = collection.find(eq("id", user_id)).first();
            mongoClient.close();
            System.out.println("Пользователь не существует. Добавляю.");
            return fn;
        } else {
            System.out.println("Пользователь существует.");
            Document fn = collection.find(eq("id", user_id)).first();
            mongoClient.close();
            return fn;
        }
    }
    
    public void onUpdateReceived(Update update) {

    	// 1
    	if(update.hasMessage()){

        	String user_first_name = update.getMessage().getChat().getFirstName();
        	String user_last_name = update.getMessage().getChat().getLastName();
        	//String user_username = update.getMessage().getChat().getUserName();
        	Integer user_id_m = update.getMessage().getFrom().getId();// .getChat().getId();
        	String message_text = update.getMessage().getText();
        	Integer message_id = update.getMessage().getMessageId();
        	long chat_id = update.getMessage().getChatId();
        	// проверки на вход сообщение от пользователя
        	boolean pax_num = isNumber(message_text);
        	final String stringl = update.getMessage().getText().toString();
    		int length = stringl.length();
        	
        	System.out.println("1   Вход сообщения с сообщ. от ползователя: "+update.getMessage().getText()+" "+update.getMessage());
        	if(update.getMessage().hasText()){
        		System.out.println("1.1 "+update.getMessage());
        		//1.1.1
                if(update.getMessage().getText().equals("/start")){ //+
                	// Проверяет один раз при старте
                	Document doc = check(user_id_m, update);
                    try {
                    	Integer user_pax = doc.getInteger("pax");
                    	Integer user_kvo = doc.getInteger("kvo");
                        if (user_pax != null) {
                        }else { 
                        	user_pax = 0;
                        }
                        if (user_kvo != null) {
                        }else { 
                        	 user_kvo = 0;
                        }
                    	//Запускает начальній єкран старт
                        execute(sendInlineKeyBoardMessageStart(chat_id, user_first_name, user_last_name, user_pax, user_kvo));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //1.1.2
                else if(update.getMessage().getText().equals("/paxyHok")){ //+
//                	Document doc = (Document) info(user_id_m);
//                	Integer user_pax = doc.getInteger("pax");
                    try {
//                        if (user_pax != null) {
//                        }else { 
//                        	user_pax = 0;
//                        }
                        execute(sendInlineKeyBoardMessagePaxyHok(update.getMessage().getChatId()));
                        } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //1.1.3
                else if((update.getMessage().getText().equals("/noroDa"))|(update.getMessage().getText().contains("огода"))
                	   |(update.getMessage().getText().contains("eather"))){ //+
                	// нужно будет выводить в отдельный класс с использованием вебхук
                    try {
                    	execute( new SendMessage().setChatId(update.getMessage().getChatId()).setText("Переглянути погоду у Вашому місті? (у розробці)"));
                    	//curl https://api.telegram.org/bot628535430:AAHgkSp6HMqgw3ILsCMS8kQjzZZiZqfuJt4/setWebhook -F "url=https://us-central1-itsm365-weather-68bc8.cloudfunctions.net/itsm365Weather
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //1.1.4
                // пока условие на вход 3 символьных слов Должно 8!!!!
                else if ((pax_num == true) & (stringl != null) & ((length < 4)) & (length > 2)) {
                	System.out.println("длина вх. сообщ.: "+stringl.length());
                	System.out.println("1.1.4 введення номеру рахунку з поля вводу (текст:"+update.getMessage().getText()+") "+update.getMessage());
                	
                	String answerS = update.getMessage().getText(); // получаем номер счета и остальные данные
                	int answer = Integer.parseInt(answerS);
                	            	    
            	    add_pax(user_id_m,answer,update.getMessage().getChatId()); //добавляем счет в БД
            	    
                	Document doc = (Document) info(user_id_m);
                	Integer user_pax = doc.getInteger("pax");
                    if (user_pax != null) {}else {user_pax = 0;}

                    try {
                		execute( new SendMessage().setChatId(update.getMessage().getChatId()).setText("Ваш рахунок № "+user_pax+" збережено."));
                		execute(sendInlineKeyBoardMessagePaxyHok(update.getMessage().getChatId()));
                	} catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                	}
                //1.1.5
                else if(update.getMessage().getText().equals("/help")){ //+
                	System.out.println("1.1.5 /help: "+update.getMessage());
             	   	try {
                	   execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Я вмію виконувати: "
                			   		+ "\n/start - натисни для початку роботи"
                           			+ "\n/paxyHok - робота з рахунком"
                           			+ "\n/noroDa - дізнайся ;) (гео локація)"
                           			+ "\n/help - допомога "
                           			+ "\n а також вмію спілкуватися ;)")) ;
                   	} catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //1.1.6
                else if ((update.getMessage().getText().contains("ривет"))| (update.getMessage().getText().contains("ривіт"))
                		| (update.getMessage().getText().contains("hbdtn"))| (update.getMessage().getText().contains("hbdsn"))
                		| (update.getMessage().getText().equals("Hi"))| (update.getMessage().getText().equals("hi"))
                		| (update.getMessage().getText().contains("Hallo"))){ //+
                	System.out.println("1.1.6 привет: "+update.getMessage());
             	   	try {
                	   execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Доброго і тобі здоров'я. Мене звуть 'Помічник', чим Вам допомогти? ")) ;
                   	} catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //1.1.7
                else if ((update.getMessage().getText().contains("Бот"))| (update.getMessage().getText().contains("бот"))
                		|(update.getMessage().getText().contains("<jn"))| (update.getMessage().getText().contains(",jn"))
                		|(update.getMessage().getText().contains("Bot"))| (update.getMessage().getText().contains("bot"))){ //+
                	System.out.println("1.1.7 бот: "+update.getMessage());
             	   	try {
                	   execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Не полюбляю цього слова, я Ваш 'Помічник' - так краще!")) ;
                   	} catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                //replyToMessage text='Updated message text'
                //1.1.8
                else if (update.getMessage().isReply()){
                	System.out.println("1.1.8 відповідь з показником: "+update.getMessage().getText()+" "+update.getMessage());
                	System.out.println("1.1.8 відповідь з показником: "+update.getMessage().getReplyToMessage().getText());
                	//споймал ответ от ползователя 
                }
                //1.1.9
                else if ((pax_num == true) & (stringl != null) & ((length < 3)) & (length > 1)) {
                	System.out.println("1.1.9 введення показників ліч №? з поля вводу (текст:"+update.getMessage().getText()+") "+update.getMessage());
                	
                	String answerS = update.getMessage().getText(); 
                	int answer = Integer.parseInt(answerS);
                	
                	lich_value3 = message_text;
//                	AnswerInlineQuery new_message new AnswerInlineQuery()
//                	.setSwitchPmText(answerS)         					
                	
//                    EditMessageText new_message = new EditMessageText()
//                            .setChatId(chat_id)
//                            .setMessageId(message_id)
//                            .setText(answerS);//.enableMarkdown(true)//.setInlineMessageId(getBaseUrl())
                    // после нажатия на кнопку скрываетвсе кнопки и делает репост text=''      
//                  EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
//                  		.setChatId(chat_id)
//                  		.setMessageId(message_id);
                	
                	// отправить в личОКкенсел
                	//забить номер лич. который вводится в локал перем

        	        
//        	        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        	        
//        	        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();
//        	        
//        	        InputTextMessageContent inputMessageContent = new InputTextMessageContent().setMessageText("55");
//        	        
//					results.add((InlineQueryResult) new InlineQueryResultArticle().setReplyMarkup(inlineKeyboardMarkup)
//        	        		.setId("1").setTitle("npo6a")
//        	        		.setInputMessageContent(inputMessageContent));
//					results.add((InlineQueryResult) new InlineQueryResultArticle().setReplyMarkup(inlineKeyboardMarkup)
//        	        		.setId("2").setTitle("npo6a6")
//        	        		.setInputMessageContent(inputMessageContent));
//					
//          			String try1 = "5555";

                  	try {
                  		execute(sendInlineKeyBoardMessageLichOkCancel(chat_id,message_text));
//						String switchPmParameter = "456";
//						execute(new AnswerInlineQuery().setSwitchPmParameter(stringl)//.setSwitchPmParameter(switchPmParameter).setSwitchPmText(answerS)
//								.setInlineQueryId(try1).setResults(results).setCacheTime(5000));
                  	} catch (TelegramApiException e) {
                  		System.out.println("Error "+e.getMessage()); 
                  		e.printStackTrace();
                  	}
                }	
                //1.1.99
                else {
                	//text='лічильники змінюються'                	
            	   System.out.println("1.1.99 Msg ОБРОБИТИ нове невідоме повідомлення від користувача: "+update.getMessage().getText()+" "+update.getMessage());
            	   try {
            	   execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Вибачте, я Вас не зрозумів.\n поки що я вмію виконувати: "
            			   		+ "\n/start - натисни для початку роботи"
                       			+ "\n/paxyHok - робота з рахунком"
                       			+ "\n/noroDa - дізнайся ;) (гео локація)"
                       			+ "\n/help - допомога")) ;
               	} catch (TelegramApiException e) {
                    e.printStackTrace();
                }
               	}
        		}
        	//1.2
        	else {
        		System.out.println("1.2 ДОРОБИТИ повідомлення без текста: "+update.getCallbackQuery());}
//*********************************************************************************************************        	
        // 2	
        }else if(update.hasCallbackQuery()){
        	
        	String user_first_name = update.getCallbackQuery().getMessage().getChat().getFirstName();
        	String user_last_name = update.getCallbackQuery().getMessage().getChat().getLastName();
        	//String user_username = update.getCallbackQuery().getFrom().getUserName();
        	Integer user_id_q = update.getCallbackQuery().getFrom().getId();
        	Integer message_id = update.getCallbackQuery().getMessage().getMessageId();//?
        	String message_text = update.getCallbackQuery().getMessage().getText();
        	long chat_id = update.getCallbackQuery().getMessage().getChatId();
        	String data_text = update.getCallbackQuery().getData(); 
        	
        	
        	System.out.println("2 CallbackQuery СТАРТ: ");//+update.getCallbackQuery());
        	// 2.1
        	if (update.getCallbackQuery().getData().contains("Редагувати лічильники")) { //+
      		        		
        		System.out.println("2.1.1 Входящее сообщение с CallbackQuery 12: "+update.getCallbackQuery());
            	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
                List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
                keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Один").setCallbackData("pax_kvo1"));
                keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Два").setCallbackData("pax_kvo2"));
                keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Три").setCallbackData("pax_kvo3"));
                keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Чотири").setCallbackData("pax_kvo4"));
                keyboardButtonsRow2.add(new InlineKeyboardButton().setText("П'ять").setCallbackData("pax_kvo5"));
                keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Шість").setCallbackData("pax_kvo6"));
                List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
                rowList.add(keyboardButtonsRow1);rowList.add(keyboardButtonsRow2);
                inlineKeyboardMarkup.setKeyboard(rowList);
                
                EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                		.setChatId(chat_id)
                		.setMessageId(message_id);

                try {
                	execute(new_message);
                	//выводит сообщение с 6 кнопками
                    execute( new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Скільки, у Вас  лічільників?").setReplyMarkup(inlineKeyboardMarkup));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
        		}
        	//2.2
            else if (update.getCallbackQuery().getData().equals("/help")){ // пока ответ на меню "Щось ще" - переправляем на список команд
                	    System.out.println("2.1.3 HELP:"+update.getCallbackQuery());
                        try {
                        	execute(new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId())
                        			.setText("Зараз я вмію виконувати: "
                        	   		+ "\n/start - натисни для початку роботи"
                        			+ "\n/paxyHok - робота з рахунком"
                        			+ "\n/noroDa - дізнайся ;) (гео локація)"
                        			+ "\n/help - допомога")) ;
///start - start
///paxyHok - paxyHok
///noroDa - noroDa
///help - help
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
               	}
           //2.3
          	else if (update.getCallbackQuery().getMessage().getText().contains("Перевірте, Ваш рахунок")) //+
        			{	
        	    System.out.println("2.3 Ответ на ваш рах збереж :"+update.getCallbackQuery());
                try {
                    execute(new SendMessage().setText(   // отправляет = Ваш рахунок №10 збережений.
                            update.getCallbackQuery().getData())
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                    //запускает старт меню Рахунка
                    if (update.getCallbackQuery().getData().contains("Ваш рахунок №")) {
                        	execute(sendInlineKeyBoardMessagePaxyHok(update.getCallbackQuery().getMessage().getChatId()));
                    }} catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        	//2.4
        	else if (update.getCallbackQuery().getData().contains("pax_kvo")){ //+
        	    System.out.println("2.4 CallbackQuery Ответ к-сть ліч 11:"+update.getCallbackQuery());

        	    Integer lich = 0;
        	    String text = ""; 
        	    if (update.getCallbackQuery().getData().contains("1")) {
        	    	 lich = 1;
        	    	 text = " лічільник.";}
        	    else if (update.getCallbackQuery().getData().contains("2")) {
        	    	 lich = 2;
        	    	 text = " лічільника.";}
        	    else if (update.getCallbackQuery().getData().contains("3")) {
        	    	 lich = 3;
        	    	 text = " лічільника.";}
        	    else if (update.getCallbackQuery().getData().contains("4")) {
        	    	 lich = 4;
        	    	 text = " лічільника.";}
        	    else if (update.getCallbackQuery().getData().contains("5")) {
        	    	 lich = 5;
        	    	 text = " лічільників.";}
        	    else if (update.getCallbackQuery().getData().contains("6")) {
        	    	 lich = 6;
        	    	 text = " лічільників.";}
        	    else {
        	    	lich = 0;
        	    	text = " лічільників.";}

				add_lich(user_id_q, lich);
        	    
        	    Object doc = info(update.getCallbackQuery().getFrom().getId());
        	    Integer user_pax = ((Document) doc).getInteger("pax");

        	    String answer = "Збережено Ваших "+lich;
                if (user_pax != null) {}else {user_pax = 0;}
                
                EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                		.setChatId(chat_id)
                		.setMessageId(message_id);

                try {
                	execute(new_message);
					execute(new SendMessage().setText(answer+text)
                            				 .setChatId(chat_id));
                    //стартовий єкран
					execute(sendInlineKeyBoardMessageLich(chat_id));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        	//2.5
        	else if (update.getCallbackQuery().getData().equals("/help")){ // пока ответ на меню "Допомога" - переправляем на список команд
        	    System.out.println("2.5 HELP:"+update.getCallbackQuery());
                try {
                	execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Зараз я вмію виконувати: "
                	   		+ "\n/start-start для початку роботи"
                			+ "\n/рахунок-робота з рахунком"
                			+ "\n/погода_на_сьогодні-дізнайся ;) (треба надати гео локацію)")) ;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        	//2.6
        	else if (update.getCallbackQuery().getMessage().getText().contains("Доступ до вашого рахунку")){
        		if (update.getCallbackQuery().getData().equals("Редагувати рахунок")) {
        			System.out.println("2.6.1 Входящее сообщение с CbQuery Редагувати рахунок: "+update.getCallbackQuery());	
                    InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                    List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Змінити").setCallbackData("Редагувати рахунок"));
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Повернутися").setCallbackData("Повернутися до роботи з рахунком"));
                    List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
                    rowList.add(keyboardButtonsRow1);
                    inlineKeyboardMarkup.setKeyboard(rowList);
                    try {
            			Object doc = info(user_id_q);
            			Integer user_pax = ((Document) doc).getInteger("pax");
            			if (user_pax != null) {}else {user_pax = 0;}
                        	execute( new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Ваш рахунок №: "+user_pax).setReplyMarkup(inlineKeyboardMarkup));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        		else if (update.getCallbackQuery().getData().equals("Видалити рахунок")){
        			System.out.println("2.6.2 Входящее сообщение с CbQuery Видалити рахунок: "+update.getCallbackQuery());
                    try {
                        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Видалити").setCallbackData("Видалити рахунок"));
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Повернутися").setCallbackData("Повернутися до роботи з рахунком"));
                        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
                        rowList.add(keyboardButtonsRow1);
                        inlineKeyboardMarkup.setKeyboard(rowList);
            			Object doc = info(user_id_q);
            			Integer user_pax = ((Document) doc).getInteger("pax");
            			if (user_pax != null) {}else {user_pax = 0;}
                        execute( new SendMessage().setChatId(update.getCallbackQuery().getMessage().getChatId()).setText("Ваш рахунок : "+user_pax).setReplyMarkup(inlineKeyboardMarkup));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        		else if (update.getCallbackQuery().getData().equals("Інформація про рахунок")){
        			System.out.println("2.6.3 Входящее сообщение с CbQuery Інфо про рахунок: "+update.getCallbackQuery());
        			Object doc = info(user_id_q);
        			Integer user_pax = ((Document) doc).getInteger("pax");
                    try {
                    	if (user_pax != null) {}else {user_pax = 0;}
                        execute(new SendMessage().setText("Ваш рахунок №: "+user_pax)
                                .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        		else if (update.getCallbackQuery().getData().equals("Повернутися до головного меню")){
        			System.out.println("2.6.4 Входящее сообщение с CbQuery повернутися до головного меню: "+update.getCallbackQuery());

                    
            			Object doc = info(user_id_q);
            			Integer user_pax = ((Document) doc).getInteger("pax");
            			Integer user_kvo = ((Document) doc).getInteger("kvo");
            			if (user_pax != null) {}else {user_pax = 0;}
            			if (user_kvo != null) {}else {user_kvo = 0;}
                        try {
                        execute(sendInlineKeyBoardMessageStart(update.getCallbackQuery().getMessage().getChatId(), user_first_name, user_last_name, user_pax, user_kvo));
                        } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        		else {
        			System.out.println("2.99.9 ДОРОБИТИ: "+update.getCallbackQuery());

        		}
        	}
        	//2.7
        	else if ((update.getCallbackQuery().getData().equals("Редагувати рахунок")) 
        		   & (update.getCallbackQuery().getMessage().getText().contains("Ваш рахунок №:"))) {
        		
        			System.out.println("2.7 ваш рах = редагувати: "+update.getCallbackQuery());
        			
                    try {
                    	execute(new SendMessage().setText("Введіть рахунок у форматі ###")
                                .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        	//2.8
        	else if (update.getCallbackQuery().getData().equals("Повернутися до роботи з рахунком")){
        			System.out.println("2.8 ваш рах = повернення до рахунку: "+update.getCallbackQuery());	
                    EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                    		.setChatId(chat_id)
                    		.setMessageId(message_id);

                    try {
                    	execute(new_message);
                        execute(sendInlineKeyBoardMessagePaxyHok(update.getCallbackQuery().getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        	//2.9
        	else if (update.getCallbackQuery().getData().equals("Видалити рахунок")){
        			System.out.println("2.9.3 ваш рах = видалити: "+update.getCallbackQuery());	
                    EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                    		.setChatId(chat_id)
                    		.setMessageId(message_id);
                    try {
                    	execute(new_message);
            			del(user_id_q);
                        execute(new SendMessage().setText("Ваш рахунок видалений.")
                                .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                        execute(sendInlineKeyBoardMessagePaxyHok(update.getCallbackQuery().getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        	}
        	//2.10
        	else if (update.getCallbackQuery().getData().equals("Вкажіть кількість лічильників.")){
        			System.out.println("2.10 ваші ліч = повернення до лічильників: "+update.getCallbackQuery());	
                	Document doc = (Document) info(user_id_q);
                	Integer user_kvo = doc.getInteger("kvo");
                    try {
                    	if (user_kvo != null) {}else {user_kvo = 0;}
                        	execute(sendInlineKeyBoardMessageLich(update.getCallbackQuery().getMessage().getChatId()));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        	//2.11-2.12
        	//2.13
        	//text='Замінити назву лічильника №
        	else if (update.getCallbackQuery().getData().contains("cold") | update.getCallbackQuery().getData().contains("hot")){
        			System.out.println("2.13 ваші ліч = перейменувати : "+data_text+" - "+message_text+" - "+update.getCallbackQuery());	
        	        String s = update.getCallbackQuery().getMessage().getText();        
        	        String lich_num = s.replaceAll("[^0-9]+", "");
        	        //System.out.println(s1);	
    		        Calendar c = Calendar.getInstance();			
    		        c.setTimeInMillis(update.getCallbackQuery().getMessage().getDate() *1000L);// 1385355600000l
        			// принимаем изм. название счетчика и записіваем в базу! 
    		        String lich_name = null;
        			if (data_text.equals("k_cold")) { 
        				lich_name = "Кухня Хол.";
        			}
        			else if (data_text.equals("k_hot")) { 
        				lich_name = "Кухня Гар.";
        			}	
        			else if (data_text.equals("b_cold")) { 
        				lich_name = "Ванна Хол.";
        			}
        			else if (data_text.equals("b_hot")) { 
        				lich_name = "Ванна Гар.";
        			}
        			else if (data_text.equals("a_cold")) { 
        				lich_name = "Додатково Хол.";
        			}
        			else if (data_text.equals("a_hot")) {
        				lich_name = "Додатково Гар.";
        			}
    		        Integer YY = c.get(Calendar.YEAR);
    		        Integer MM = c.get(Calendar.MONTH);
    		        Integer DD = c.get(Calendar.DATE);
    		        String value_date = DD.toString()+MM.toString()+YY.toString();
        				//rename_lich(user_id_q, data_text, lich_name, chat_id, "lich"+s1, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        				save_value_lich(user_id_q, lich_num, lich_value3, value_date, lich_name, true);
        				
                    try {
                       	execute(sendInlineKeyBoardMessageCreateLich(chat_id,user_id_q,message_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        	//2.14
        	else if (update.getCallbackQuery().getMessage().getText().contains("Доступ до ваших лічильників:")){
        		System.out.println("2.14 ваші ліч = Доступ до ваших лічильників: : "+update.getCallbackQuery());
        		if (update.getCallbackQuery().getData().equals("Повернутися до головного меню")){
        			System.out.println("2.14.1 ваш ліч = повернення до гол.меню: "+update.getCallbackQuery());	
        			try {
        				Document doc = (Document) info(user_id_q);
        				Integer user_kvo = doc.getInteger("kvo");
        				Integer user_pax = doc.getInteger("pax");
        				// 	execute(sendInlineKeyBoardMessageLich(chat_id));
        				execute(sendInlineKeyBoardMessageStart(chat_id, user_first_name, user_last_name,user_pax, user_kvo));
        			} catch (TelegramApiException e) {
        				e.printStackTrace();
        			}
        		}
        		//2.14.2
            	else if (update.getCallbackQuery().getData().equals("Ввести показники")){
        			System.out.println("2.14.2 ваші ліч = Ввести показники: "+update.getCallbackQuery());	
                    EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                    		.setChatId(chat_id)
                    		.setMessageId(message_id);
                    try {
                    	execute(new_message);
                    	execute(sendInlineKeyBoardMessageCreateLich(chat_id, user_id_q,message_id));
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
            	//2.14.3
            	//text='Доступ до ваших лічильників:'
            	else if (update.getCallbackQuery().getData().contains("змінити")){
            			System.out.println("2.14.3 ваші ліч = перейменувати : "+update.getCallbackQuery());	
            			//шукаємо номер лічильника
            	        String s = update.getCallbackQuery().getData();        
            	        String s1 = s.replaceAll("[^0-9]+", "");
            	        System.out.println(s1);  
                        EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                        		.setChatId(chat_id)
                        		.setMessageId(message_id);

                        try {
                        	execute(new_message);
                           	execute(sendInlineKeyBoardMessageReNameLich(update.getCallbackQuery().getMessage().getChatId(), s1));
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
            		}
        		//2.14.4
            	//text='Доступ до ваших лічильників:'data='показники1'
        		//Вилавлюємо натиск кнопок внесення показників
            	else if (update.getCallbackQuery().getData().contains("показники")){
        	        String s = update.getCallbackQuery().getData();        
        	        String s1 = s.replaceAll("[^0-9]+", "");
        	        System.out.println(s1);
        			System.out.println("2.14.4 ваші ліч = внести показник №: "+s1+update.getCallbackQuery());	
        			lich_num = s1;

        			String answer = "ввести показник лічильника № "+s1+"\n у форматі **";

                    EditMessageText new_message = new EditMessageText()
                            .setChatId(chat_id)
                            .setMessageId(message_id)
                            .setText(answer)
                            ;
                    try {
                        execute(new_message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
        		}
        		//2.14.5
            	else if (update.getCallbackQuery().getData().contains("Інформація про лічильники")){
            		mes2 = "";
                	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
                	List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
                	
                    //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
                	MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
                    MongoClient mongoClient = new MongoClient(connectionString);
                	@SuppressWarnings("deprecation")
            		DB db = mongoClient.getDB("db_voda");
                	DBCollection coll = db.getCollection("users");
                	BasicDBObject searchQuery = new BasicDBObject("id", user_id_q);//.append("lich1.lichname", "641400902545024");
                	DBCursor cursor = coll.find(searchQuery);
                	
                	while(cursor.hasNext()) {
                	    DBObject currDocument = cursor.next();
                	    System.out.println("find info -"+currDocument.get("kvo").toString());
                	    String user_kvo1 = currDocument.get("kvo").toString();
                	    int user_kvo = Integer.parseInt(user_kvo1);
                	    	for (int i = 1; i <= user_kvo; i++) {
                	    	try {
                	    		Object probe = currDocument.get("lich"+i);
                	    		//Object probe2 = ((BasicBSONObject) probe).get("lich_name");  // = lich_name
                	    		String name = ((BSONObject) probe).get("lich_name").toString();
                	    		Object p_p = currDocument.get("lich"+i);
                	    		p_p = ((BasicBSONObject) p_p).get("lich_value");
                	    		String p_p1 = ((BasicBSONObject) p_p).get("value").toString();
                	    		String p_p2 = ((BSONObject) p_p).get("date").toString();
                	    		if (i != user_kvo) {
                	    			mes1 = i+". " + name+"\n       показник: "+p_p1+"\n       від: "+p_p2+"\n";
                	    		}
                	    		else {
                	    			mes1 = i+". " + name+"\n       показник: "+p_p1+"\n       від: "+p_p2;
                	    			System.out.println("find-05-"+(mes1));
                	    		}
                	    	}
                	    	finally {    
                	    	}
                    	    mes2 = mes2 + mes1;	
                    	    //System.out.println("find-06-"+(mes2));
                	    }
                	}	
    				try {
    					execute(new SendMessage(chat_id,mes2));
                    
    				} catch (TelegramApiException e) {
    					e.printStackTrace();
    				}
            	}
            	else {
            		System.out.println("2.14.9 ДОРОБИТИ : "+"Дата :"+update.getCallbackQuery().getData().toString()+" Text :"+update.getCallbackQuery().getMessage().getText().toString()+" - "+update.getCallbackQuery());
            	}
    		}
        	//2.15
        	//text='Перевірте чи вірно!'   data='save_value_lich' data='return_lich_menu'
    		//Вилавлюємо натиск кнопок внесення показників
        	else if (message_text.contains("Перевірте чи вірно!")){
    			System.out.println("2.15 обробка вводу показників №: "+lich_value3+update.getCallbackQuery());	

    			if (data_text.equals("return_lich_menu")) {
    				System.out.println("2.15.1 повернення до меню ліч: "+lich_value3+update.getCallbackQuery());	
    
    				EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                		.setChatId(chat_id)
                		.setMessageId(message_id);

    				try {
    					execute(new_message);
    					execute(sendInlineKeyBoardMessageLich(chat_id));
                    
    				} catch (TelegramApiException e) {
    					e.printStackTrace();
    				}
    			}
    			else if (data_text.equals("save_value_lich")) {
    				System.out.println("2.15.2 повернення до меню ліч: "+lich_value3+update.getCallbackQuery());
    				
    				EditMessageReplyMarkup new_message = new EditMessageReplyMarkup()
                		.setChatId(chat_id)
                		.setMessageId(message_id);
    		        Calendar c = Calendar.getInstance();			
    		        c.setTimeInMillis(update.getCallbackQuery().getMessage().getDate() *1000L);
    		        Integer YY = c.get(Calendar.YEAR);
    		        Integer MM = c.get(Calendar.MONTH);
    		        Integer DD = c.get(Calendar.DATE);
    		        String value_date = DD.toString()+MM.toString()+YY.toString();
    				save_value_lich(user_id_q,lich_num,lich_value3,value_date,null,false);
    				try {
    					execute(new_message);
    					execute(new SendMessage(chat_id,"Значення показника № "+lich_num+" ("+lich_value3+")"+"  збережено!"));
    					execute(sendInlineKeyBoardMessageLich(chat_id));
                    
    				} catch (TelegramApiException e) {
    					e.printStackTrace();
    				}
    			}
    		}
        	//2.99 // Иначе с CallbackQuery
            else {
            	if (update.hasChosenInlineQuery()){
            		System.out.println("Входящее сообщение с CallbackQuery 2.99.1: "+update.getCallbackQuery());	
            	}
            	else if (update.hasInlineQuery()) {
            		System.out.println("Входящее сообщение с CallbackQuery 2.99.2: "+update.getCallbackQuery());
            	}	
            	
            	else {
            	System.out.println("Входящее сообщение с CallbackQuery 2.99.3: "+update.getCallbackQuery());
            	}
            	}
        }
    	//3.0
        else {
            	System.out.println("3.1 ДОРОБИТИ ЩОСЬ НОВЕ ;): "+update.getCallbackQuery());
        	}  	
    }

//    public void editMessage(BotTextMessage msg, String channelTo, String messageId) {
//        String messageText = BotsController.messageFormatter(
//                msg.getBotFrom(), msg.getChannelFrom(), msg.getNicknameFrom(),
//                Optional.ofNullable(msg.getText()));
//        EditMessageText text = new EditMessageText();
//        text.setChatId(channelTo);
//        text.setMessageId(Integer.parseInt(messageId));
//        text.setText(messageText);
//
//        try {
//            execute(text);
//        } catch (TelegramApiException e) {
//            logger.info("Message text not found, trying to edit that as a caption. ", e);
//
//            EditMessageCaption caption = new EditMessageCaption();
//            caption.setChatId(channelTo);
//            caption.setMessageId(Integer.parseInt(messageId));
//            caption.setCaption(messageText);
//
//            try {
//                execute(caption);
//            } catch (TelegramApiException e1) {
//                logger.error("Error while changing img caption. ", e1);
//            }
//        }
//    }
    
	public String getBotUsername() {
    	return botUserName; 
    }

    @Override
    public String getBotToken() {
        return token;
    }

    public boolean isNumber(String str) {
        if (str == null || str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) return false;
        }
        return true;
    }
    public static SendMessage sendInlineKeyBoardMessageStart(long chatId, String user_first_name, String user_last_name, Integer user_pax, Integer user_kvo2) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Рахунок").setCallbackData("Повернутися до роботи з рахунком");
        inlineKeyboardButton2.setText("Лічильники та показники").setCallbackData("Вкажіть кількість лічильників.");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(inlineKeyboardButton1);keyboardButtonsRow1.add(inlineKeyboardButton2);
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Допомога").setCallbackData("/help"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return new SendMessage().setChatId(chatId).setText("Привіт "+user_first_name+" "+user_last_name+", я бот від КП Черкасиводоканал."+"\n" + 
        		"Інформація користувача:"+"\n" +"Рахунок №: "+user_pax+"\n" +"К-сть лічильників: "+user_kvo2+"\n" +
        		"Для продовження роботи виберіть потрібний пункт").setReplyMarkup(inlineKeyboardMarkup);
    }

    public static SendMessage sendInlineKeyBoardMessagePaxyHok(long chatId) {
    	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        String switchInlineQuery = "не зрозумів";
		keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Редагувати рахунок").setCallbackData("Редагувати рахунок").setSwitchInlineQuery(switchInlineQuery));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Видалити рахунок").setCallbackData("Видалити рахунок"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Інформація про рахунок").setCallbackData("Інформація про рахунок"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Повернутися до головного меню").setCallbackData("Повернутися до головного меню"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
    	return (new SendMessage().setChatId(chatId).setText("Доступ до вашого рахунку").setReplyMarkup(inlineKeyboardMarkup));
    }	
    
    public static SendMessage sendInlineKeyBoardMessageLichOkCancel(long chatId, String message_text) {
	
    	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    	List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
    	keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Зберегти").setCallbackData("save_value_lich"));
    	keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Повернутися").setCallbackData("return_lich_menu"));
    	List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
    	rowList.add(keyboardButtonsRow1);
    	inlineKeyboardMarkup.setKeyboard(rowList);
    	return (new SendMessage().setChatId(chatId).setText("Перевірте чи вірно! Лічильник №"+lich_num+"показник: "+message_text).setReplyMarkup(inlineKeyboardMarkup));
    }	
    public static SendMessage sendInlineKeyBoardMessageLich(long chatId) {
    	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
        List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
        String switchInlineQuery = "не зрозумів";
		keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Редагувати к-сть лічильників").setCallbackData("Редагувати лічильники").setSwitchInlineQuery(switchInlineQuery));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Ввести показники").setCallbackData("Ввести показники"));
        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Інформація про лічильники та показники").setCallbackData("Інформація про лічильники"));
        keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Повернутися до головного меню").setCallbackData("Повернутися до головного меню"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        inlineKeyboardMarkup.setKeyboard(rowList);
    	return (new SendMessage().setChatId(chatId).setText("Доступ до ваших лічильників:").setReplyMarkup(inlineKeyboardMarkup));
    }
    
    public static SendMessage sendInlineKeyBoardMessageCreateLich(long chatId, Integer user_id, Integer message_id){
    	String l1_name = "Лічильник 1";
    	Document doc = (Document) info(user_id);
    	Integer user_kvo = doc.getInteger("kvo");
    	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    	List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
    	
        //MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
    	MongoClientURI connectionString = new MongoClientURI("mongodb+srv://DypukUa:m15842308!@cluster0-3tubd.gcp.mongodb.net/test?retryWrites=true");
        MongoClient mongoClient = new MongoClient(connectionString);
    	@SuppressWarnings("deprecation")
		DB db = mongoClient.getDB("db_voda");
    	DBCollection coll = db.getCollection("users");
    	BasicDBObject searchQuery = new BasicDBObject("id", user_id);//.append("lich1.lichname", "641400902545024");
    	DBCursor cursor = coll.find(searchQuery);
    	
    	while(cursor.hasNext()) {
    	    DBObject currDocument = cursor.next();
    	    for (int i = 1; i <= user_kvo; i++) {
    	    	try {
    	    		BasicDBObject newDocument = new BasicDBObject("lich"+i, currDocument.get("lich"+i));
    	    		
    	    		System.out.println("find-"+i+"-"+((DBObject)newDocument.get("lich"+i)));
    	    		if ((DBObject)newDocument.get("lich"+i) == null ) {l1_name = "Лічильник"+i;}
    	    		else {
    	    		l1_name = ((DBObject)newDocument.get("lich"+i)).get("lich_name").toString();}

    	    		List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
    	    		keyboardButtonsRow1.add(new InlineKeyboardButton().setText(l1_name).setCallbackData("змінити"+i));
    	    		keyboardButtonsRow1.add(new InlineKeyboardButton().setText(l1_name+" внести показники").setCallbackData("показники"+i));
    	    		rowList.add(keyboardButtonsRow1);
    	    	}
    	    	finally {    
    	    	}
    	    }
    	}
    	List<InlineKeyboardButton> keyboardButtonsRow7 = new ArrayList<InlineKeyboardButton>();
    	keyboardButtonsRow7.add(new InlineKeyboardButton().setText("Повернутися").setCallbackData("Вкажіть кількість лічильників."));
    	rowList.add(keyboardButtonsRow7);
    	inlineKeyboardMarkup.setKeyboard(rowList);
    	cursor.close();mongoClient.close();
    	return (new SendMessage().setChatId(chatId).setText("Доступ до ваших лічильників:").setReplyMarkup(inlineKeyboardMarkup));//.setReplyToMessageId(message_id));
    }
    public static SendMessage sendInlineKeyBoardMessageReNameLich(long chatId , String lich_num_re) {
    	InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
    	List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
    	List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
   		keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Кухня Хол.").setCallbackData("k_cold"));
   		keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Кухня Гар.").setCallbackData("k_hot"));
   		rowList.add(keyboardButtonsRow1);
    	List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>();
    	keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Ванна Хол.").setCallbackData("b_cold"));
		keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Ванна Гар.").setCallbackData("b_hot"));
		rowList.add(keyboardButtonsRow2);	
    	List<InlineKeyboardButton> keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>();
    	keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Додатково Хол.").setCallbackData("a_cold"));
		keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Додатково Гар.").setCallbackData("a_hot"));
		rowList.add(keyboardButtonsRow3);	
		inlineKeyboardMarkup.setKeyboard(rowList);

    	return (new SendMessage().setChatId(chatId).setText("Замінити назву лічильника №"+lich_num_re).setReplyMarkup(inlineKeyboardMarkup));
    }
    	
}

