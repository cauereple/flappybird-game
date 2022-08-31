package game.flappybird;

public class Passaro {
    
    public double x, y;
    
    //VELOCIDADE NO EIXO Y
    public double vy = 0;

    //GRAVIDADE
    public static double G = 700;

    public static double FLAP = -300;

    public Passaro(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void atualiza(double dt) {
        vy += G*dt;
        y += vy*dt;
    }

    public void flap() {
        vy = FLAP;
    }

    public void desenhar(Tela tela) {
        tela.imagem("flappy.png", 528, 128, 34, 24, Math.atan(vy/500), x, y);
    }
}
