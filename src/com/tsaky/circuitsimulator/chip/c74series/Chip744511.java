package com.tsaky.circuitsimulator.chip.c74series;

import com.tsaky.circuitsimulator.InfoPage;
import com.tsaky.circuitsimulator.chip.Chip;
import com.tsaky.circuitsimulator.chip.pin.*;

public class Chip744511 extends Chip {

    public Chip744511(){
        super("744511",
                new InfoPage("BCD to 7-Segment Decode", "ic744511.png"),
                new Pin[]{
                new Pin("B", 0, PinType.INPUT),
                new Pin("C", 1, PinType.INPUT),
                new Pin("LT", 2, PinType.INPUT),
                new Pin("BL", 3, PinType.INPUT),
                new Pin("LE", 4, PinType.INPUT),
                new Pin("D", 5, PinType.INPUT),
                new Pin("A", 6, PinType.INPUT),
                new Pin("GND", 7, PinType.GROUND),
                new Pin("e", 8, PinType.OUTPUT),
                new Pin("d", 9, PinType.OUTPUT),
                new Pin("c", 10, PinType.OUTPUT),
                new Pin("b", 11, PinType.OUTPUT),
                new Pin("a", 12, PinType.OUTPUT),
                new Pin("g", 13, PinType.OUTPUT),
                new Pin("f", 14, PinType.OUTPUT),
                new Pin("VCC", 15, PinType.POWER)
        });
        setSize(40, 200);
    }

    @Override
    public void calculateOutputs() {
        if(!isPowered()){
            turnAllPinTypesTo(PinType.OUTPUT, PinType.HIGH_Z);
        }else{
            turnAllPinTypesTo(PinType.HIGH_Z, PinType.OUTPUT);
            boolean A = getPin(6).isLinkHigh();
            boolean B = getPin(0).isLinkHigh();
            boolean C = getPin(1).isLinkHigh();
            boolean D = getPin(5).isLinkHigh();
            boolean LE = getPin(4).isLinkHigh();
            boolean LT = getPin(2).isLinkHigh();
            boolean BL = getPin(3).isLinkHigh();
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

            getPin(12).setHigh(a);
            getPin(11).setHigh(b);
            getPin(10).setHigh(c);
            getPin(9).setHigh(d);
            getPin(8).setHigh(e);
            getPin(14).setHigh(f);
            getPin(13).setHigh(g);

        }
    }

}
