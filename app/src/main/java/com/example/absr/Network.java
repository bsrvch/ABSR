package com.example.absr;

public class Network {
    public double[][][][] w1 = new double[64][9][9][1];
    public double[][][][] w2 = new double[32][1][1][64];
    public double[][][][] w3 = new double[1][5][5][32];
    public double[] b1 = new double[64];
    public double[] b2 = new double[32];
    public double[] b3 = new double[1];
    public Network(){
        w1 = random_parameters_weight(w1);
        w2 = random_parameters_weight(w2);
        w3 = random_parameters_weight(w3);
        b1 = random_parameters_bias(b1);
        b2 = random_parameters_bias(b2);
        b3 = random_parameters_bias(b3);
    }
    public double[][][] conv(double[][][] in, double[][][][]f, double[]b, String activation){
        //=========================================//
        int FiMC = f.length;//filter max count
        int FiMH = f[0].length;//filter max height
        int FiMW = f[0][0].length;//filter max width
        int FiMD = f[0][0][0].length;//filter max deep
        //=========================================//
        int InMH = in.length;//input max height
        int InMW = in[0].length;//input max width
        int InMD = in[0][0].length;//input max deep
        //=========================================//
        double[][][] out = new double[InMH-FiMH+1][InMW-FiMW+1][FiMC];
        //=========================================//
        for(int fci = 0; fci < FiMC; fci++){//filter count iteration
            for(int ihi = 0; ihi < InMH-FiMH+1; ihi++){//input height iteration
                for(int iwi = 0; iwi < InMW-FiMW+1; iwi++) {//input width iteration
                    double a = 0;
                    for (int fhi = 0; fhi < FiMH; fhi++) {//filter height iteration
                        for (int fwi = 0; fwi < FiMW; fwi++) {//filter width iteration
                            for (int di = 0; di < InMD; di++) {//deep iteration
                                a += in[ihi + fhi][iwi + fwi][di] * f[fci][fhi][fwi][di];
                            }
                        }
                    }
                    a += b[fci];
                    out[ihi][iwi][fci] += a;
                }
            }
        }
        return out;
    }
    public double[][][][] weights_back(double[][][]in, double[][][]f){
        int InMH = in.length;//input max height
        int InMW = in[0].length;//input max width
        int InMD = in[0][0].length;//input max deep
        //=========================================//
        int FiMH = f.length;//filter max height
        int FiMW = f[0].length;//filter max width
        int FiMD = f[0][0].length;//filter max deep
        //=========================================//
        double[][][][] out = new double[FiMD][InMH-FiMH+1][InMW-FiMW+1][InMD];
        //=========================================//
        for(int fdi = 0; fdi < FiMD; fdi++){//filter deep iteration
            for (int idi = 0; idi < InMD; idi++) {//input deep iteration
                for(int ihi = 0; ihi < InMH-FiMH+1; ihi++) {//input height iteration
                    for (int iwi = 0; iwi < InMW - FiMW + 1; iwi++) {//input width iteration
                        double a = 0;
                        for (int fhi = 0; fhi < FiMH; fhi++) {//filter height iteration
                            for (int fwi = 0; fwi < FiMW; fwi++) {//filter width iteration
                                a += in[ihi + fhi][iwi + fwi][idi] * f[fhi][fwi][fdi];
                            }
                        }
                        out[fdi][ihi][iwi][idi] += a;
                    }
                }
            }
        }
        return out;
    }
    public double[][][] input_back(double[][][]in, double[][][][]f){
        int FiMC = f.length;//filter max count
        int FiMH = f[0].length;//filter max height
        int FiMW = f[0][0].length;//filter max width
        int FiMD = f[0][0][0].length;//filter max deep
        in=zeroarr(in,(FiMH-1)/2);

        //=========================================//
        int InMH = in.length;//input max height
        int InMW = in[0].length;//input max width
        int InMD = in[0][0].length;//input max deep
        //=========================================//
        double[][][] out = new double[InMH-FiMH+1][InMW-FiMW+1][FiMD];
        //=========================================//
        for(int fdi = 0; fdi < FiMD; fdi++){//filter count iteration
            for(int ihi = 0; ihi < InMH-FiMH+1; ihi++){//input height iteration
                for(int iwi = 0; iwi < InMW-FiMW+1; iwi++) {//input width iteration
                    double a = 0;
                    for (int fhi = 0; fhi < FiMH; fhi++) {//filter height iteration
                        for (int fwi = 0; fwi < FiMW; fwi++) {//filter width iteration
                            for (int di = 0; di < InMD; di++) {//deep iteration
                                a += in[ihi + fhi][iwi + fwi][di] * f[di][FiMH-fhi-1][FiMW-fwi-1][fdi];
                            }
                        }
                    }

                    out[ihi][iwi][fdi] +=a;
                }
            }
        }
        return out;
    }
    public double[] bias_back(double[]b,double[][][]a){

        for(int i = 0; i<b.length;i++){
            double wb=0;
            for(int x=0;x<a.length;x++){
                for(int y = 0;y<a.length;y++){
                    wb+=a[x][y][i];
                }
            }
            b[i]-=wb*0.000001;
        }
        return b;
    }

    public double[][][] activation_back(double[][][] in){
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                for(int z = 0; z < in[0][0].length; z++){
                    if(in[i][j][z]>=0){
                        in[i][j][z] *= 1;
                    }
                    else{
                        in[i][j][z] *= 0;
                    }
                }
            }
        }
        return in;
    }
    public double [][][][] getRotateArr1(double [][][][] w) {
        double[][][][] retArr = new double[w.length][w[0].length][w[0][0].length][w[0][0][0].length];
        for(int i=0;i<w.length;i++){
            for(int j=0;j<w[0].length;j++){
                for(int z=0;z<w[0][0].length;z++){
                    for(int x=0;x<w[0][0][0].length;x++){// j   z
                        retArr[i][z][w[0].length-1-j][x] = w[i][j][z][x];
                    }
                }
            }
        }

        return retArr;
    }
    public double [][][] getRotateArr(double [][][] w) {
        double[][][] retArr = new double[w.length][w[0].length][w[0][0].length];

        for(int j=0;j<w.length;j++){
            for(int z=0;z<w[0].length;z++){
                for(int x=0;x<w[0][0].length;x++){// j   z
                    retArr[z][w[0].length-1-j][x] = w[j][z][x];
                }
            }
        }


        return retArr;
    }
    public double[][][] zeroarr(double [][][] in,int padding){
        double[][][] out = new double[in[0].length+padding*2][in[0].length+padding*2][in[0][0].length];

        for(int j=0;j<out[0][0].length;j++){
            for(int z=0;z<out.length;z++){
                for(int x=0;x<out[0].length;x++){
                    if(z<padding || x<padding || z>in[0].length+padding-1 || x>in[0].length+padding-1){//
                        out[z][x][j]=0;
                    }
                    else{
                        out[z][x][j]=in[z-padding][x-padding][j];
                    }
                }
            }
        }

        return out;
    }

    public double [][][] norm(double [][][] w) {


        for(int j=0;j<w.length;j++){
            for(int z=0;z<w[0].length;z++){
                for(int x=0;x<w[0][0].length;x++){// j   z
                    w[j][z][x]=Math.min(255,w[j][z][x]);
                    w[j][z][x]=Math.max(0,w[j][z][x]);
                }
            }
        }


        return w;
    }





    private double[][][][] random_parameters_weight(double[][][][] in){
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                for(int z = 0; z < in[0][0].length; z++){
                    for(int w = 0; w < in[0][0][0].length; w++){
                        in[i][j][z][w] = Math.abs(Math.random()*0.0001);
                    }
                }
            }
        }
        return in;
    }
    private double[] random_parameters_bias(double[] in){
        for(int i = 0; i < in.length; i++){
            in[i] = 0;//Math.random()*0.0001;
        }
        return in;
    }

}

