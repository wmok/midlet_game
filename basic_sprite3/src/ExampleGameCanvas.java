import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class ExampleGameCanvas extends GameCanvas implements Runnable {
	private boolean isPlay; // Game Loop runs when isPlay is true
	private long delay; // To give thread consistency
	private int currentX, currentY; // To hold current position of the 'X'
	private int scnX, scnY; // To hold current position of the 'X'
	private int initScnX, initScnY; // initial position of the 'X'
	private int width; // To hold screen width
	private int height; // To hold screen height

	// Sprites to be used
	Image backgroundImage;
	private Sprite backgroundSprite;
	// Layer Manager
	private LayerManager layerManager;
	
	// Sprites to be used
	private Sprite sprite;
	private Sprite nonTransparentSprite;
	
	// Constructor and initialization
	public ExampleGameCanvas() throws Exception {
		super(true);
		scnX = 20;
		scnY = 20;
		delay = 20;
		initScnX = scnX;
		initScnY = scnY;
		
		// Load Images to Sprites
		// Load Images to Sprites
		Image image = Image.createImage("/sprite.png");
		sprite = new Sprite (image,32,32);
		Image imageTemp = Image.createImage("/sprite.png");
		nonTransparentSprite = new Sprite (imageTemp,32,32);
		
		backgroundImage = Image.createImage("/background2.png");
		backgroundSprite = new Sprite(backgroundImage);
		
		width = initScnX+140;
		height = initScnY+140;
		
		currentX = scnX;
		currentY = scnY;
		
		int x = backgroundImage.getWidth();
		
		layerManager = new LayerManager();
		layerManager.append(backgroundSprite);
	}
	// Automatically start thread for game loop
	public void start() {
		isPlay = true;
		Thread t = new Thread(this);
		t.start();
	}
	
	public void stop() { isPlay = false; }
	
	// Main Game Loop
	public void run() {
		int frame = 0;
		int tmpFrame;
		Graphics g = getGraphics();
		while (isPlay == true) {
			tmpFrame = input();
			
			if (tmpFrame != 0)
			{
				frame = tmpFrame;
			}
			
			sprite.setFrame(frame);
			drawScreen(g);
			
			try { Thread.sleep(delay); }

			catch (InterruptedException ie) {}
		}
	}
	// Method to Handle User Inputs
	private int input() {
		int frame = 0;
		int keyStates = getKeyStates();
		if ((keyStates & LEFT_PRESSED) != 0) {
			if (scnX - 1 > 20)
				scnX--;
			
			currentX = Math.max(scnX, currentX - 1);
			
			sprite.setFrame(1);
			frame = 1;
		}
		
		if ((keyStates & RIGHT_PRESSED) != 0) {
			if (scnX + 1 + 140 < backgroundImage.getWidth())
				scnX++;
						
			currentX = Math.min(scnX+20, currentX + 1);
			sprite.setFrame(3);
			frame = 3;
			// }
		}
		
		// Up
		if ((keyStates & UP_PRESSED) != 0) {
			//System.out.println("up");
			currentY = Math.max(initScnY, currentY - 1);
			sprite.setFrame(2);
			frame = 2;
		}
		// Down
		if ((keyStates & DOWN_PRESSED) !=0)
			//System.out.println("down");
			if ( currentY + 10 < initScnY+120) {
				currentY = Math.min(initScnY+120, currentY + 1);
				
				sprite.setFrame(4);
				frame = 4;
			}
		
		return frame;
	}
	// Method to Display Graphics
	private void drawScreen(Graphics g) {
		//g.setColor(0x00C000);
		g.setColor(0xffffff);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(0x0000ff);
		// display all layers
		layerManager.setViewWindow(scnX,scnY,140,140);
		layerManager.paint(g,20,20);
		
		sprite.setPosition(currentX,currentY);
		sprite.paint(g);
		nonTransparentSprite.setPosition(currentX,currentY);
		nonTransparentSprite.paint(g);
		
		flushGraphics();
	}
}