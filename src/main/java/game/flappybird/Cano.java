package game.flappybird;

public class Cano {
    
    public double x;
    public int stage_height;
    public double vxcano;

    public Cano(double x, int sh, double vx) {
        this.x = x;
        this.vxcano = vx;
        this.stage_height = sh;
    }

    public void atualiza(double dt) {

    }

    public void desenha(Tela tela) {

        tela.imagem("flappy.png", 604, 0, 52, 270, 0, x, 0);
    }
}
