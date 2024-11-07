// Initialize Phaser Game Configuration
const config = {
    type: Phaser.AUTO,
    width: 800,
    height: 600,
    scene: {
        preload: preload,
        create: create,
        update: update
    }
};

// Create the game instance
const game = new Phaser.Game(config);

// Declare game variables for player, enemy, and controls
let player;
let enemy;
let cursors;
let attackButton;
let attackSound;

// Preload function to load assets
function preload() {
    // Load images for background, player, and enemy
    this.load.image('background', 'assets/images/background.png');
    this.load.image('player', 'assets/images/player.png');
    this.load.image('enemy', 'assets/images/enemy.png');
    
    // Load attack sound effect
    this.load.audio('attack-sound', 'assets/sounds/attack-sound.mp3');
}

// Create function to initialize game objects and controls
function create() {
    // Add background image
    this.add.image(400, 300, 'background');
    
    // Create player and enemy sprites
    player = this.physics.add.sprite(100, 500, 'player');
    player.setCollideWorldBounds(true);

    enemy = this.physics.add.sprite(700, 500, 'enemy');
    enemy.setCollideWorldBounds(true);
    
    // Enable player controls
    cursors = this.input.keyboard.createCursorKeys();
    
    // Create attack button (e.g., 'A' key)
    attackButton = this.input.keyboard.addKey(Phaser.Input.Keyboard.KeyCodes.A);
    
    // Load attack sound
    attackSound = this.sound.add('attack-sound');
    
    // Add physics collider for simple interaction (e.g., hit detection)
    this.physics.add.collider(player, enemy, handleAttack, null, this);
}

// Update function to handle game logic and movement
function update() {
    // Player movement
    if (cursors.left.isDown) {
        player.setVelocityX(-160); // Move left
    }
    else if (cursors.right.isDown) {
        player.setVelocityX(160); // Move right
    }
    else {
        player.setVelocityX(0); // Stop horizontal movement
    }

    if (cursors.up.isDown) {
        player.setVelocityY(-160); // Move up
    }
    else if (cursors.down.isDown) {
        player.setVelocityY(160); // Move down
    }
    else {
        player.setVelocityY(0); // Stop vertical movement
    }

    // Attack trigger
    if (Phaser.Input.Keyboard.JustDown(attackButton)) {
        player.setVelocityX(0); // Stop movement during attack
        player.setVelocityY(0);
        attack(player.x, player.y); // Trigger attack action
    }
}

// Attack function to handle animation and effects
function attack(x, y) {
    player.setAlpha(0.5); // Make player semi-transparent during attack
    attackSound.play(); // Play attack sound
    
    // For simplicity, we just make the player blink for attack effect
    this.tweens.add({
        targets: player,
        alpha: 1,
        duration: 200,
        ease: 'Power1',
        onComplete: () => {
            // Reset alpha after attack
            player.setAlpha(1);
        }
    });
    
    // Example simple collision detection for attack (can be expanded with hitboxes)
    if (Phaser.Math.Distance.Between(x, y, enemy.x, enemy.y) < 50) {
        // Simple effect: enemy loses health or moves back
        enemy.setTint(0xff0000); // Enemy flashes red on hit
        enemy.setVelocityX(-100); // Push the enemy back slightly
        setTimeout(() => {
            enemy.clearTint(); // Reset enemy tint after 0.5 seconds
        }, 500);
    }
}
