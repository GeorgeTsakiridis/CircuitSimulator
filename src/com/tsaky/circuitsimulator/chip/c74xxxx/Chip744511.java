package com.tsaky.circuitsimulator.chip.c74xxxx;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip744511 extends Chip {

    public Chip744511(){
        super("744511",
                new InfoPage("BCD to 7-Segment Decode", "ic744511.png"),
                new Pin[]{
                new PinInput("B", 0),
                new PinInput("C", 1),
                new PinInput("LT", 2),
                new PinInput("BL", 3),
                new PinInput("LE", 4),
                new PinInput("D", 5),
                new PinInput("A", 6),
                new PinGround("GND", 7),
                new PinOutput("e", 8),
                new PinOutput("d", 9),
                new PinOutput("c", 10),
                new PinOutput("b", 11),
                new PinOutput("a", 12),
                new PinOutput("g", 13),
                new PinOutput("f", 14),
                new PinPower("VCC", 15)
        });
        setSize(40, 200);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllOutputsOff();
            turnAllOutputsToHighZ();
        }else{

            boolean A = getInputPin(6).isLinkHigh();
            boolean B = getInputPin(0).isLinkHigh();
            boolean C = getInputPin(1).isLinkHigh();
            boolean D = getInputPin(5).isLinkHigh();
            boolean LE = getInputPin(4).isLinkHigh();
            boolean LT = getInputPin(2).isLinkHigh();
            boolean BL = getInputPin(3).isLinkHigh();
            boolean a = false;
            boolean b = false;
            boolean c = false;
            boolean d = false;
            boolean e = false;
            boolean f = false;
            boolean g = false;

            if(!LE && LT && BL){
                if(!A && !B && !C && !D){ //0
                    a=b=c=d=e=f=true;
                }
                else if(A && !B && !C && !D){ //1
                    b=c=true;
                }
                else if(!A && B && !C && !D){ //2
                    a=b=d=e=g=true;
                }
                else if(A && B && !C && !D){ //3
                    a=b=c=d=g=true;
                }
                else if(!A && !B && C && !D){ //4
                    b=c=f=g=true;
                }
                else if(A && !B && C && !D){ //5
                    a=c=d=f=g=true;
                }
                else if(!A && B && C && !D){ //6
                    c=d=e=f=g=true;
                }
                else if(A && B && C && !D){ //7
                    a=b=c=true;
                }
                else if(!A && !B && !C && D){ //8
                    a=b=c=d=e=f=g=true;
                }
                else if(A && !B && !C && D){ //9
                    a=b=c=f=g=true;
                }
            }else if(!LT){
                a=b=c=d=e=f=g=true;
            }

            getOutputPin(12).setHigh(a);
            getOutputPin(11).setHigh(b);
            getOutputPin(10).setHigh(c);
            getOutputPin(9).setHigh(d);
            getOutputPin(8).setHigh(e);
            getOutputPin(14).setHigh(f);
            getOutputPin(13).setHigh(g);

        }
    }

}
