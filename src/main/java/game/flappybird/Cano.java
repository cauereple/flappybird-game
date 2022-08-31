package game.flappybird;

public class Cano {
    
    public double x, y;
    public double vxcano = - 100;
    public static int HOLESIZE = 100;
    public boolean counted = false;

    public Hitbox boxcima;
    public Hitbox boxbaixo;

    public Cano(double x, double y) {
        this.x = x;
        this.y = y;

        boxcima = new Hitbox(x, y - 270-220, x + 52, y);
        boxbaixo = new Hitbox(x, y + HOLESIZE, x + 52, y + HOLESIZE + 442);
    }

    public void atualiza(double dt) {
        x += vxcano*dt;
        boxcima.mover(vxcano*dt, 0);
        boxbaixo.mover(vxcano*dt, 0);
    }

    public void desenha(Tela tela) {

        //CANO DE CIMA
        tela.imagem("flappy.png", 604, 0, 52, 270, 0, x, y - 270);

        tela.imagem("flappy.png", 604, 0, 52, 220, 0, x, y - 270 - 220);

        //CANO DE BAIXO
        tela.imagem("flappy.png", 660, 0, 52, 242, 0, x, y + HOLESIZE);

        tela.imagem("flappy.png", 660, 42, 52, 200, 0, x, y + HOLESIZE + 242);
    }
}
