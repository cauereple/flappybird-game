package game.flappybird;

public class Passaro {
    
    public double x, y;
    public int largura = 34;
    public int altura = 24;
    
    //VELOCIDADE NO EIXO Y
    public double vy = 0;

    //GRAVIDADE
    public static double G = 700;

    public static double FLAP = -250;

    public Hitbox box;
    public Timer spriteTimer;
    public int spriteState = 0;
    public int[] spriteStates = {0, 1, 2, 1};
    public int[] spritesX = {528, 528, 446};
    public int[] spritesY = {128, 180, 248};

    private Acao mudaSprite() {
        return new Acao() {
            public void executa() {
                spriteState ++;
                spriteState = spriteState%spriteStates.length;
            }
        };
    }

    public Passaro(double x, double y) {
        this.x = x;
        this.y = y;
        this.box = new Hitbox(x , y, x + largura, y + altura);
        spriteTimer = new Timer(0.1, true, mudaSprite());
    }

    public void atualiza(double dt) {
        vy += G*dt;
        y += vy*dt;

        box.mover(0, vy*dt);
    }

    public void updateSprite(double dt) {
        spriteTimer.tique(dt);
    }

    public void desenhar(Tela tela) {
        tela.imagem("flappy.png", spritesX[spriteStates[spriteState]], spritesY[spriteStates[spriteState]], largura, altura, Math.atan(vy/200), x, y);
    }

    public void flap() {
        vy = FLAP;
    }
}
