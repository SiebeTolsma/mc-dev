package net.minecraft.server;

import java.util.Random;

public class BlockFlowing extends BlockFluids {

    int a = 0;
    boolean[] b = new boolean[4];
    int[] c = new int[4];

    protected BlockFlowing(int i, Material material) {
        super(i, material);
    }

    private void k(World world, int i, int j, int k) {
        int l = world.getData(i, j, k);

        world.setTypeIdAndData(i, j, k, this.id + 1, l, 2);
    }

    public boolean b(IBlockAccess iblockaccess, int i, int j, int k) {
        return this.material != Material.LAVA;
    }

    public void a(World world, int i, int j, int k, Random random) {
        int l = this.k_(world, i, j, k);
        byte b0 = 1;

        if (this.material == Material.LAVA && !world.worldProvider.e) {
            b0 = 2;
        }

        boolean flag = true;
        int i1;

        if (l > 0) {
            byte b1 = -100;

            this.a = 0;
            int j1 = this.d(world, i - 1, j, k, b1);

            j1 = this.d(world, i + 1, j, k, j1);
            j1 = this.d(world, i, j, k - 1, j1);
            j1 = this.d(world, i, j, k + 1, j1);
            i1 = j1 + b0;
            if (i1 >= 8 || j1 < 0) {
                i1 = -1;
            }

            if (this.k_(world, i, j + 1, k) >= 0) {
                int k1 = this.k_(world, i, j + 1, k);

                if (k1 >= 8) {
                    i1 = k1;
                } else {
                    i1 = k1 + 8;
                }
            }

            if (this.a >= 2 && this.material == Material.WATER) {
                if (world.getMaterial(i, j - 1, k).isBuildable()) {
                    i1 = 0;
                } else if (world.getMaterial(i, j - 1, k) == this.material && world.getData(i, j - 1, k) == 0) {
                    i1 = 0;
                }
            }

            if (this.material == Material.LAVA && l < 8 && i1 < 8 && i1 > l && random.nextInt(4) != 0) {
                i1 = l;
                flag = false;
            }

            if (i1 == l) {
                if (flag) {
                    this.k(world, i, j, k);
                }
            } else {
                l = i1;
                if (i1 < 0) {
                    world.setAir(i, j, k);
                } else {
                    world.setData(i, j, k, i1, 2);
                    world.a(i, j, k, this.id, this.a(world));
                    world.applyPhysics(i, j, k, this.id);
                }
            }
        } else {
            this.k(world, i, j, k);
        }

        if (this.o(world, i, j - 1, k)) {
            if (this.material == Material.LAVA && world.getMaterial(i, j - 1, k) == Material.WATER) {
                world.setTypeIdUpdate(i, j - 1, k, Block.STONE.id);
                this.fizz(world, i, j - 1, k);
                return;
            }

            if (l >= 8) {
                this.flow(world, i, j - 1, k, l);
            } else {
                this.flow(world, i, j - 1, k, l + 8);
            }
        } else if (l >= 0 && (l == 0 || this.n(world, i, j - 1, k))) {
            boolean[] aboolean = this.m(world, i, j, k);

            i1 = l + b0;
            if (l >= 8) {
                i1 = 1;
            }

            if (i1 >= 8) {
                return;
            }

            if (aboolean[0]) {
                this.flow(world, i - 1, j, k, i1);
            }

            if (aboolean[1]) {
                this.flow(world, i + 1, j, k, i1);
            }

            if (aboolean[2]) {
                this.flow(world, i, j, k - 1, i1);
            }

            if (aboolean[3]) {
                this.flow(world, i, j, k + 1, i1);
            }
        }
    }

    private void flow(World world, int i, int j, int k, int l) {
        if (this.o(world, i, j, k)) {
            int i1 = world.getTypeId(i, j, k);

            if (i1 > 0) {
                if (this.material == Material.LAVA) {
                    this.fizz(world, i, j, k);
                } else {
                    Block.byId[i1].c(world, i, j, k, world.getData(i, j, k), 0);
                }
            }

            world.setTypeIdAndData(i, j, k, this.id, l, 3);
        }
    }

    private int d(World world, int i, int j, int k, int l, int i1) {
        int j1 = 1000;

        for (int k1 = 0; k1 < 4; ++k1) {
            if ((k1 != 0 || i1 != 1) && (k1 != 1 || i1 != 0) && (k1 != 2 || i1 != 3) && (k1 != 3 || i1 != 2)) {
                int l1 = i;
                int i2 = k;

                if (k1 == 0) {
                    l1 = i - 1;
                }

                if (k1 == 1) {
                    ++l1;
                }

                if (k1 == 2) {
                    i2 = k - 1;
                }

                if (k1 == 3) {
                    ++i2;
                }

                if (!this.n(world, l1, j, i2) && (world.getMaterial(l1, j, i2) != this.material || world.getData(l1, j, i2) != 0)) {
                    if (!this.n(world, l1, j - 1, i2)) {
                        return l;
                    }

                    if (l < 4) {
                        int j2 = this.d(world, l1, j, i2, l + 1, k1);

                        if (j2 < j1) {
                            j1 = j2;
                        }
                    }
                }
            }
        }

        return j1;
    }

    private boolean[] m(World world, int i, int j, int k) {
        int l;
        int i1;

        for (l = 0; l < 4; ++l) {
            this.c[l] = 1000;
            i1 = i;
            int j1 = k;

            if (l == 0) {
                i1 = i - 1;
            }

            if (l == 1) {
                ++i1;
            }

            if (l == 2) {
                j1 = k - 1;
            }

            if (l == 3) {
                ++j1;
            }

            if (!this.n(world, i1, j, j1) && (world.getMaterial(i1, j, j1) != this.material || world.getData(i1, j, j1) != 0)) {
                if (this.n(world, i1, j - 1, j1)) {
                    this.c[l] = this.d(world, i1, j, j1, 1, l);
                } else {
                    this.c[l] = 0;
                }
            }
        }

        l = this.c[0];

        for (i1 = 1; i1 < 4; ++i1) {
            if (this.c[i1] < l) {
                l = this.c[i1];
            }
        }

        for (i1 = 0; i1 < 4; ++i1) {
            this.b[i1] = this.c[i1] == l;
        }

        return this.b;
    }

    private boolean n(World world, int i, int j, int k) {
        int l = world.getTypeId(i, j, k);

        if (l != Block.WOODEN_DOOR.id && l != Block.IRON_DOOR_BLOCK.id && l != Block.SIGN_POST.id && l != Block.LADDER.id && l != Block.SUGAR_CANE_BLOCK.id) {
            if (l == 0) {
                return false;
            } else {
                Material material = Block.byId[l].material;

                return material == Material.PORTAL ? true : material.isSolid();
            }
        } else {
            return true;
        }
    }

    protected int d(World world, int i, int j, int k, int l) {
        int i1 = this.k_(world, i, j, k);

        if (i1 < 0) {
            return l;
        } else {
            if (i1 == 0) {
                ++this.a;
            }

            if (i1 >= 8) {
                i1 = 0;
            }

            return l >= 0 && i1 >= l ? l : i1;
        }
    }

    private boolean o(World world, int i, int j, int k) {
        Material material = world.getMaterial(i, j, k);

        return material == this.material ? false : (material == Material.LAVA ? false : !this.n(world, i, j, k));
    }

    public void onPlace(World world, int i, int j, int k) {
        super.onPlace(world, i, j, k);
        if (world.getTypeId(i, j, k) == this.id) {
            world.a(i, j, k, this.id, this.a(world));
        }
    }

    public boolean l() {
        return false;
    }
}
