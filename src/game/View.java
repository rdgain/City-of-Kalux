package game;

import utilities.Constants;

import javax.swing.*;
import java.awt.*;

import static utilities.Constants.*;

public class View extends JComponent {
	KaluxGame game;
    Image intf;
    Image img;
    int countE, countS;
    String reply, reply1, reply2, cut1, cut2;
    int inputDelay;
    boolean increase, next, ach, notebook, menu, content1, content2, content3, content4, content5, n_next, prev;
    int idx;

    public View(KaluxGame game) {
        this.game = game;
        countE = 0;
        countS = 0;
        reply = "";
        reply1 = "";
        reply2 = "";
        cut1 = "";
        cut2 = "";
        inputDelay = 10;
        increase = false;
        next = false;
        idx = 0;
    }

	@Override
	public void paintComponent(Graphics g0) {

        Graphics2D g = (Graphics2D) g0;

        // paint the background
        //g.setColor(BG_COLOR);
        //g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        // draw layers : layer 1, layer 2, layer 3
         ///* // check for player room to draw only objects they can see
        for (GameObject i : game.worldObjectsL1) {
            if (i instanceof Decor) {
                Decor o = (Decor)i;
                if (o.type == 2 && !o.visited) {
                    for (Background x: o.room) {
                        if (x == game.getPlayerRoom()) {
                            i.draw(g);
                            o.visited = true;
                        }
                    }
                }
                else {
                    i.draw(g);
                }
            }
            else {
                if (i instanceof Character) {
                    if (!i.visited) {
                        for (Background x:i.room) {
                            if (x == game.getPlayerRoom()) {
                                i.draw(g);
                                i.visited = true;
                            }
                        }
                    } else {
                        i.draw(g);
                    }
                } else {
                    i.draw(g);
                }
            }
        }
        //*/
        //for (GameObject i : game.worldObjectsL1) {
        //    i.draw(g);
        //}
        for (GameObject i : game.worldObjectsL2) {
            i.draw(g);
        }
        for (GameObject i : game.worldObjectsL3) {
            i.draw(g);
        }

        //draw fog of war
        ///*
        g.setColor(Color.BLACK);
        for (int i = 0; i < game.visW; i++) {
            for (int j = 0; j < game.visH; j++) {
                if (game.vis[i][j]==1) {
                    g.fillRect(19+i*2, 19+j*2, SCALE/2, SCALE/2);
                }
            }
        }
        //*/


        // draw interface
        if (game.gotBackpack) {
            intf = INTERFACE;
        }
        else {
            intf = INTERFACENOB;
        }
        g.drawImage(intf, 0, 0, null);

        // draw game stats
        if (game.huntedMode) {
            g.setColor(Color.RED);
            g.drawString("HUNTED", FRAME_WIDTH / 4 + 177, FRAME_HEIGHT - 175);
        }
        g.setColor(Color.YELLOW);
        g.drawString("Money: " + game.player.money + "$", FRAME_WIDTH / 4 + 175, FRAME_HEIGHT - 155);
        g.drawString("Event: 1", FRAME_WIDTH / 4 + 183, FRAME_HEIGHT - 135);
        switch (game.currentMap) {
            case 1:
                g.drawString("Map: 1 {10 Chopper Way}", FRAME_WIDTH / 4 + 135, FRAME_HEIGHT - 115);
                break;
            case 2:
                g.drawString("Map: 2 {Paper Factory}", FRAME_WIDTH / 4 + 138, FRAME_HEIGHT - 115);
                break;
            case 3:
                g.drawString("Map: 3 {Hayden Park}", FRAME_WIDTH / 4 + 140, FRAME_HEIGHT - 115);
                break;
            case 4:
                g.drawString("Map: 4 {Officials' HQ}", FRAME_WIDTH / 4 + 140, FRAME_HEIGHT - 115);
                break;
            case 5:
                g.drawString("Map: 5 {Wilderness}", FRAME_WIDTH / 4 + 145, FRAME_HEIGHT - 115);
                break;
            default: break;
        }

        // draw achievements
        if (game.ctrl.action.achievements) {
            if (!ach) ach = true;
            else ach = false;
        }
        if (ach) {
            // paint background of achievements box
            g.drawImage(BOX, FRAME_WIDTH / 4, FRAME_HEIGHT / 4, null);
            // display title of box
            g.setColor(Color.YELLOW);
            g.drawString("ACHIEVEMENTS", FRAME_WIDTH / 4 + 60, FRAME_HEIGHT / 4 + 25);
            boolean ok = false;
            int height = FRAME_HEIGHT / 4 + 80;
            for (int i=0; i<Constants.NO_ACHIEVEMENTS; i++) {
                if (game.ach[i]) {
                    ok = true;
                    g.drawString(game.achN[i], FRAME_WIDTH / 4 + 40, height);
                    height += 50;
                }
            }
            if (!ok) {
                g.drawString("No achievements unlocked yet.", FRAME_WIDTH / 4 + 40, height);
            }
        }

        // draw notebook
        if (game.ctrl.action.notebook) {
            if (!notebook) {notebook = true; menu = true; idx = 0;}
            else {notebook = false; menu = false; idx = 0;}
        }
        if (notebook) {
            Font font = g.getFont();
            if (game.ctrl.action.title1) {
                content1 = true;
                menu = false;
            }
            if (game.ctrl.action.title2) {
                content2 = true;
                menu = false;
            }
            if (game.ctrl.action.title3) {
                content3 = true;
                menu = false;
            }
            if (game.ctrl.action.title4) {
                content4 = true;
                menu = false;
            }
            if (game.ctrl.action.title5) {
                content5 = true;
                menu = false;
            }
            if (game.ctrl.action.menu) {
                menu = true;
                content1 = false;
                content2 = false;
                content3 = false;
                content4 = false;
                content5 = false;
            }
            if (content1 || content2 || content3 || content5) {
                if (game.ctrl.action.prev) {prev = true; idx--;}
                if (game.ctrl.action.n_next) {n_next = true; idx++;}
            }

            // paint background of notebook
            g.drawImage(N_BOX, FRAME_WIDTH / 4, FRAME_HEIGHT / 4, null);
            // display title of box
            g.setColor(Color.YELLOW);
            g.drawString("NOTEBOOK", FRAME_WIDTH / 4 + 60, FRAME_HEIGHT / 4 + 25);
            if (menu) {
                //display titles;
                int height = FRAME_HEIGHT / 4 + 120;
                g.setFont( new Font("Arial", Font.BOLD, 18));
                for (String i : game.notebookT) {
                    g.drawString(i, FRAME_WIDTH / 4 + 100, height);
                    height += 30;
                }
            } else {
                if (idx < 0) idx = 0;
                g.setFont( new Font("Arial", Font.PLAIN, 16));
                if (content1) {
                    if (idx >= game.notebook1.size()) idx = game.notebook1.size() - 1;
                    //display first set of content
                    String st = game.notebook1.get(idx);
                    String st1 = "", st2 = "", st3 = "", st4 = "", st5 = "", st6 = "", st7 = "", st8 = "", st9 = "";
                    if (st.length() > 55) {
                        st1 = st.substring(0, 55);
                        int x = st1.lastIndexOf(' ');
                        st1 = st1.substring(0, x);
                        st2 = st.substring(x+1);
                        st = st2;
                        if (st2.length() > 55) {
                            st2 = st.substring(0, 55);
                            x = st2.lastIndexOf(' ');
                            st2 = st2.substring(0, x);
                            st3 = st.substring(x+1);
                            st = st3;
                            if (st3.length() > 55) {
                                st3 = st.substring(0, 55);
                                x = st3.lastIndexOf(' ');
                                st3 = st3.substring(0, x);
                                st4 = st.substring(x+1);
                                st = st4;
                                if (st4.length() > 55) {
                                    st4 = st.substring(0, 55);
                                    x = st4.lastIndexOf(' ');
                                    st4 = st4.substring(0, x);
                                    st5 = st.substring(x+1);
                                    st = st5;
                                    if (st5.length() > 55) {
                                        st5 = st.substring(0, 55);
                                        x = st5.lastIndexOf(' ');
                                        st5 = st5.substring(0, x);
                                        st6 = st.substring(x+1);
                                        st = st6;
                                        if (st6.length() > 55) {
                                            st6 = st.substring(0, 55);
                                            x = st6.lastIndexOf(' ');
                                            st6 = st6.substring(0, x);
                                            st7 = st.substring(x+1);
                                            st = st7;
                                            if (st7.length() > 55) {
                                                st7 = st.substring(0, 55);
                                                x = st7.lastIndexOf(' ');
                                                st7 = st7.substring(0, x);
                                                st8 = st.substring(x+1);
                                                st = st8;
                                                if (st8.length() > 55) {
                                                    st8 = st.substring(0, 55);
                                                    x = st8.lastIndexOf(' ');
                                                    st8 = st8.substring(0, x);
                                                    st9 = st.substring(x+1);
                                                } else {
                                                    st9 = "";
                                                }
                                            } else {
                                                st8 = "";st9 = "";
                                            }
                                        } else {
                                            st7 = "";st8 = "";st9 = "";
                                        }
                                    } else {
                                        st6 = "";st7 = "";st8 = "";st9 = "";
                                    }
                                } else {
                                    st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                                }
                            } else {
                                st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                            }
                        } else {
                            st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                        }
                    } else {
                        st1 = st;st2 = "";st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                    }
                    g.drawString(st1, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 100);
                    g.drawString(st2, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 120);
                    g.drawString(st3, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 140);
                    g.drawString(st4, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 160);
                    g.drawString(st5, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 180);
                    g.drawString(st6, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 200);
                    g.drawString(st7, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 220);
                    g.drawString(st8, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 240);
                    g.drawString(st9, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 260);
                }
                if (content2 && !game.notebook2.isEmpty()) {
                    if (idx >= game.notebook2.size()) idx = game.notebook2.size() - 1;
                    //display first set of content
                    String st = game.notebook2.get(idx);
                    String st1 = "", st2 = "", st3 = "", st4 = "", st5 = "", st6 = "", st7 = "", st8 = "", st9 = "";
                    if (st.length() > 55) {
                        st1 = st.substring(0, 55);
                        int x = st1.lastIndexOf(' ');
                        st1 = st1.substring(0, x);
                        st2 = st.substring(x+1);
                        st = st2;
                        if (st2.length() > 55) {
                            st2 = st.substring(0, 55);
                            x = st2.lastIndexOf(' ');
                            st2 = st2.substring(0, x);
                            st3 = st.substring(x+1);
                            st = st3;
                            if (st3.length() > 55) {
                                st3 = st.substring(0, 55);
                                x = st3.lastIndexOf(' ');
                                st3 = st3.substring(0, x);
                                st4 = st.substring(x+1);
                                st = st4;
                                if (st4.length() > 55) {
                                    st4 = st.substring(0, 55);
                                    x = st4.lastIndexOf(' ');
                                    st4 = st4.substring(0, x);
                                    st5 = st.substring(x+1);
                                    st = st5;
                                    if (st5.length() > 55) {
                                        st5 = st.substring(0, 55);
                                        x = st5.lastIndexOf(' ');
                                        st5 = st5.substring(0, x);
                                        st6 = st.substring(x+1);
                                        st = st6;
                                        if (st6.length() > 55) {
                                            st6 = st.substring(0, 55);
                                            x = st6.lastIndexOf(' ');
                                            st6 = st6.substring(0, x);
                                            st7 = st.substring(x+1);
                                            st = st7;
                                            if (st7.length() > 55) {
                                                st7 = st.substring(0, 55);
                                                x = st7.lastIndexOf(' ');
                                                st7 = st7.substring(0, x);
                                                st8 = st.substring(x+1);
                                                st = st8;
                                                if (st8.length() > 55) {
                                                    st8 = st.substring(0, 55);
                                                    x = st8.lastIndexOf(' ');
                                                    st8 = st8.substring(0, x);
                                                    st9 = st.substring(x+1);
                                                } else {
                                                    st9 = "";
                                                }
                                            } else {
                                                st8 = "";st9 = "";
                                            }
                                        } else {
                                            st7 = "";st8 = "";st9 = "";
                                        }
                                    } else {
                                        st6 = "";st7 = "";st8 = "";st9 = "";
                                    }
                                } else {
                                    st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                                }
                            } else {
                                st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                            }
                        } else {
                            st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                        }
                    } else {
                        st1 = st;st2 = "";st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                    }
                    g.drawString(st1, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 100);
                    g.drawString(st2, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 120);
                    g.drawString(st3, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 140);
                    g.drawString(st4, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 160);
                    g.drawString(st5, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 180);
                    g.drawString(st6, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 200);
                    g.drawString(st7, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 220);
                    g.drawString(st8, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 240);
                    g.drawString(st9, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 260);
                }
                if (content3 && !game.notebook3.isEmpty()) {
                    if (idx >= game.notebook3.size()) idx = game.notebook3.size() - 1;
                    //display first set of content
                    String st = game.notebook3.get(idx);
                    String st1 = "", st2 = "", st3 = "", st4 = "", st5 = "", st6 = "", st7 = "", st8 = "", st9 = "";
                    if (st.length() > 55) {
                        st1 = st.substring(0, 55);
                        int x = st1.lastIndexOf(' ');
                        st1 = st1.substring(0, x);
                        st2 = st.substring(x+1);
                        st = st2;
                        if (st2.length() > 55) {
                            st2 = st.substring(0, 55);
                            x = st2.lastIndexOf(' ');
                            st2 = st2.substring(0, x);
                            st3 = st.substring(x+1);
                            st = st3;
                            if (st3.length() > 55) {
                                st3 = st.substring(0, 55);
                                x = st3.lastIndexOf(' ');
                                st3 = st3.substring(0, x);
                                st4 = st.substring(x+1);
                                st = st4;
                                if (st4.length() > 55) {
                                    st4 = st.substring(0, 55);
                                    x = st4.lastIndexOf(' ');
                                    st4 = st4.substring(0, x);
                                    st5 = st.substring(x+1);
                                    st = st5;
                                    if (st5.length() > 55) {
                                        st5 = st.substring(0, 55);
                                        x = st5.lastIndexOf(' ');
                                        st5 = st5.substring(0, x);
                                        st6 = st.substring(x+1);
                                        st = st6;
                                        if (st6.length() > 55) {
                                            st6 = st.substring(0, 55);
                                            x = st6.lastIndexOf(' ');
                                            st6 = st6.substring(0, x);
                                            st7 = st.substring(x+1);
                                            st = st7;
                                            if (st7.length() > 55) {
                                                st7 = st.substring(0, 55);
                                                x = st7.lastIndexOf(' ');
                                                st7 = st7.substring(0, x);
                                                st8 = st.substring(x+1);
                                                st = st8;
                                                if (st8.length() > 55) {
                                                    st8 = st.substring(0, 55);
                                                    x = st8.lastIndexOf(' ');
                                                    st8 = st8.substring(0, x);
                                                    st9 = st.substring(x+1);
                                                } else {
                                                    st9 = "";
                                                }
                                            } else {
                                                st8 = "";st9 = "";
                                            }
                                        } else {
                                            st7 = "";st8 = "";st9 = "";
                                        }
                                    } else {
                                        st6 = "";st7 = "";st8 = "";st9 = "";
                                    }
                                } else {
                                    st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                                }
                            } else {
                                st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                            }
                        } else {
                            st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                        }
                    } else {
                        st1 = st;st2 = "";st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                    }
                    g.drawString(st1, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 100);
                    g.drawString(st2, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 120);
                    g.drawString(st3, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 140);
                    g.drawString(st4, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 160);
                    g.drawString(st5, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 180);
                    g.drawString(st6, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 200);
                    g.drawString(st7, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 220);
                    g.drawString(st8, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 240);
                    g.drawString(st9, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 260);
                }
                if (content4 && !game.notebook4.isEmpty()) {
                    if (idx >= game.notebook4.size()) idx = game.notebook4.size() - 1;
                    //display first set of content
                    String st = game.notebook4.get(idx);
                    String st1 = "", st2 = "", st3 = "", st4 = "", st5 = "", st6 = "", st7 = "", st8 = "", st9 = "";
                    if (st.length() > 55) {
                        st1 = st.substring(0, 55);
                        int x = st1.lastIndexOf(' ');
                        st1 = st1.substring(0, x);
                        st2 = st.substring(x+1);
                        st = st2;
                        if (st2.length() > 55) {
                            st2 = st.substring(0, 55);
                            x = st2.lastIndexOf(' ');
                            st2 = st2.substring(0, x);
                            st3 = st.substring(x+1);
                            st = st3;
                            if (st3.length() > 55) {
                                st3 = st.substring(0, 55);
                                x = st3.lastIndexOf(' ');
                                st3 = st3.substring(0, x);
                                st4 = st.substring(x+1);
                                st = st4;
                                if (st4.length() > 55) {
                                    st4 = st.substring(0, 55);
                                    x = st4.lastIndexOf(' ');
                                    st4 = st4.substring(0, x);
                                    st5 = st.substring(x+1);
                                    st = st5;
                                    if (st5.length() > 55) {
                                        st5 = st.substring(0, 55);
                                        x = st5.lastIndexOf(' ');
                                        st5 = st5.substring(0, x);
                                        st6 = st.substring(x+1);
                                        st = st6;
                                        if (st6.length() > 55) {
                                            st6 = st.substring(0, 55);
                                            x = st6.lastIndexOf(' ');
                                            st6 = st6.substring(0, x);
                                            st7 = st.substring(x+1);
                                            st = st7;
                                            if (st7.length() > 55) {
                                                st7 = st.substring(0, 55);
                                                x = st7.lastIndexOf(' ');
                                                st7 = st7.substring(0, x);
                                                st8 = st.substring(x+1);
                                                st = st8;
                                                if (st8.length() > 55) {
                                                    st8 = st.substring(0, 55);
                                                    x = st8.lastIndexOf(' ');
                                                    st8 = st8.substring(0, x);
                                                    st9 = st.substring(x+1);
                                                } else {
                                                    st9 = "";
                                                }
                                            } else {
                                                st8 = "";st9 = "";
                                            }
                                        } else {
                                            st7 = "";st8 = "";st9 = "";
                                        }
                                    } else {
                                        st6 = "";st7 = "";st8 = "";st9 = "";
                                    }
                                } else {
                                    st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                                }
                            } else {
                                st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                            }
                        } else {
                            st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                        }
                    } else {
                        st1 = st;st2 = "";st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                    }
                    g.drawString(st1, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 100);
                    g.drawString(st2, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 120);
                    g.drawString(st3, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 140);
                    g.drawString(st4, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 160);
                    g.drawString(st5, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 180);
                    g.drawString(st6, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 200);
                    g.drawString(st7, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 220);
                    g.drawString(st8, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 240);
                    g.drawString(st9, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 260);
                }
                if (content5 && !game.notebook5.isEmpty()) {
                    if (idx >= game.notebook5.size()) idx = game.notebook5.size() - 1;
                    //display first set of content
                    String st = game.notebook5.get(idx);
                    String st1 = "", st2 = "", st3 = "", st4 = "", st5 = "", st6 = "", st7 = "", st8 = "", st9 = "";
                    if (st.length() > 55) {
                        st1 = st.substring(0, 55);
                        int x = st1.lastIndexOf(' ');
                        st1 = st1.substring(0, x);
                        st2 = st.substring(x+1);
                        st = st2;
                        if (st2.length() > 55) {
                            st2 = st.substring(0, 55);
                            x = st2.lastIndexOf(' ');
                            st2 = st2.substring(0, x);
                            st3 = st.substring(x+1);
                            st = st3;
                            if (st3.length() > 55) {
                                st3 = st.substring(0, 55);
                                x = st3.lastIndexOf(' ');
                                st3 = st3.substring(0, x);
                                st4 = st.substring(x+1);
                                st = st4;
                                if (st4.length() > 55) {
                                    st4 = st.substring(0, 55);
                                    x = st4.lastIndexOf(' ');
                                    st4 = st4.substring(0, x);
                                    st5 = st.substring(x+1);
                                    st = st5;
                                    if (st5.length() > 55) {
                                        st5 = st.substring(0, 55);
                                        x = st5.lastIndexOf(' ');
                                        st5 = st5.substring(0, x);
                                        st6 = st.substring(x+1);
                                        st = st6;
                                        if (st6.length() > 55) {
                                            st6 = st.substring(0, 55);
                                            x = st6.lastIndexOf(' ');
                                            st6 = st6.substring(0, x);
                                            st7 = st.substring(x+1);
                                            st = st7;
                                            if (st7.length() > 55) {
                                                st7 = st.substring(0, 55);
                                                x = st7.lastIndexOf(' ');
                                                st7 = st7.substring(0, x);
                                                st8 = st.substring(x+1);
                                                st = st8;
                                                if (st8.length() > 55) {
                                                    st8 = st.substring(0, 55);
                                                    x = st8.lastIndexOf(' ');
                                                    st8 = st8.substring(0, x);
                                                    st9 = st.substring(x+1);
                                                } else {
                                                    st9 = "";
                                                }
                                            } else {
                                                st8 = "";st9 = "";
                                            }
                                        } else {
                                            st7 = "";st8 = "";st9 = "";
                                        }
                                    } else {
                                        st6 = "";st7 = "";st8 = "";st9 = "";
                                    }
                                } else {
                                    st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                                }
                            } else {
                                st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                            }
                        } else {
                            st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                        }
                    } else {
                        st1 = st;st2 = "";st3 = "";st4 = "";st5 = "";st6 = "";st7 = "";st8 = "";st9 = "";
                    }
                    g.drawString(st1, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 100);
                    g.drawString(st2, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 120);
                    g.drawString(st3, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 140);
                    g.drawString(st4, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 160);
                    g.drawString(st5, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 180);
                    g.drawString(st6, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 200);
                    g.drawString(st7, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 220);
                    g.drawString(st8, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 240);
                    g.drawString(st9, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 260);
                }
            }
            g.setFont(font);
        }

        // draw talk interaction
        if (game.player.talking) {
            game.freeze = true;
            // paint background of dialogue box
            g.drawImage(D_BOX, FRAME_WIDTH / 4, FRAME_HEIGHT / 4, null);
            // display talking character's name
            g.setColor(Color.YELLOW);
            g.drawString(game.player.character.name, FRAME_WIDTH / 4 + 60, FRAME_HEIGHT / 4 + 25);

            // display character's replies
            reply = game.answer;
            if (reply.length() > 70) {
                reply1 = reply.substring(0, 70);
                int x = reply1.lastIndexOf(' ');
                reply1 = reply1.substring(0, x);
                reply2 = reply.substring(x+1);
            } else {
                reply1 = reply;
                reply2 = "";
            }
            g.drawString(reply1, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 80);
            g.drawString(reply2, FRAME_WIDTH / 4 + 40, FRAME_HEIGHT / 4 + 100);

            // display player lines
            int height = FRAME_HEIGHT / 2 - 40;
            for (String i : game.playerLines) {
                if (i.length() > 75) {
                    cut1 = i.substring(0, 75);
                    int x = cut1.lastIndexOf(' ');
                    cut1 = cut1.substring(0, x);
                    cut2 = i.substring(x+1);
                    g.drawString(cut1, FRAME_WIDTH / 4 + 40, height);
                    g.drawString(cut2, FRAME_WIDTH / 4 + 40, height + 20);
                    height += 50;
                } else {
                    cut2 = "";
                    g.drawString(i, FRAME_WIDTH / 4 + 40, height);
                    height += 30;
                }
            }

            // display character image in bottom right corner
            g.drawImage(game.player.character.image, FRAME_WIDTH - 170, FRAME_HEIGHT - 197, null);
        }
        else {
            g.drawImage(NOCHAR, FRAME_WIDTH - 170, FRAME_HEIGHT - 195, null);
            game.freeze = false;
        }

        if (game.ctrl.action.map) {
            g.drawImage(MAP,  FRAME_WIDTH / 4 + 50, FRAME_HEIGHT / 4, null);
        }

        // display backpack
        int x = FRAME_WIDTH/4 - 45;
        int y = FRAME_HEIGHT - 190;
        for (InteractiveObject i : game.backpack) {
            g.drawImage(i.imgBP, x, y, null);
            if ((game.backpack.indexOf(i)+1)%3 == 0) {
                x = FRAME_WIDTH / 4 - 45;
                y += 42;
            }
            else {
                x += 46;
            }
        }

        // display item messages when used
        for (InteractiveObject i : game.backpack) {
            if (game.ctrl.action.use[game.backpack.indexOf(i)]) {
                if (i.content != "") {
                    g.setColor(Color.BLACK);
                    g.fillRect(FRAME_WIDTH / 4, FRAME_HEIGHT - 250, i.content.length()*10, 40);
                    g.setColor(Color.YELLOW);
                    g.drawString(i.content, FRAME_WIDTH / 4 + 30, FRAME_HEIGHT - 230);
                } else {
                    if (i.imgLarge != null) {
                        g.drawImage(i.imgLarge, FRAME_WIDTH / 4, FRAME_HEIGHT / 4, null);
                    }
                }
            }
        }

        // display stage messages
        if (game.eventStarted) {
            switch (game.event) {
                case 1:
                    switch (game.stage) {
                        case 1:
                            img = EV1S1;
                            break;
                        case 2:
                            img = EV1S2;
                            break;
                        case 3:
                            img = EV1S3;
                            break;
                        case 4:
                            img = EV1S4;
                            break;
                        case 5:
                            img = EV1S5;
                            break;
                        case 6:
                            img = EV1S6;
                            break;
                        case 8:
                            img = EV1S8;
                            break;
                        default:
                            img = null;
                            break;
                    }
                    break;
                case 2:
                    switch (game.stage) {
                        case 2:
                            img = EV1S2;
                            break;
                        default:
                            img = null;
                            break;
                    }
                    break;
                default:
                    break;
            }
            if (next) {
                img = null;
                countS = 301;
                next = false;
            }
            if (game.stageChanged && countS <= 300) {
                countS++;
                if (game.ctrl.action.next)
                    next = true;
                if (img != null)
                    g.drawImage(img, FRAME_WIDTH / 4 - 200, FRAME_HEIGHT / 2, null);
            }
            else {
                countS = 0;
                game.stageChanged = false;
            }
        } else {
            // display event intro
            switch (game.event) {
                case 1:
                    if (countE < EV1.size()) {
                        g.drawImage(EV1.get(countE), FRAME_WIDTH / 4, FRAME_HEIGHT / 4, null);
                    } else {
                        game.eventStarted = true;
                        game.stage = 1;
                        game.stageChanged = true;
                        game.freeze = false;
                        countE = 0;
                    }
                    if (game.ctrl.action.next) {
                        increase = true;
                    }
                    if (increase) {
                        if (inputDelay > 0)
                            inputDelay--;
                        else {
                            countE++;
                            inputDelay = 10;
                            increase = false;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
}