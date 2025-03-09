package com.rs.game.map;

import com.rs.game.world.entity.Entity;

public class ProjectileRoute {

    public static final int OBJECT_MASK = 0x100;

    public static final int UNMOVABLE_MASK = 0x200000;

    public static final int DECORATION_MASK = 0x40000;

    public static final int WEST_MASK = 0x1240108, EAST_MASK = 0x1240180;

    public static final int SOUTH_MASK = 0x1240102, NORTH_MASK = 0x1240120;

    public static final int SOUTH_WEST_MASK = 0x124010e, NORTH_WEST_MASK = 0x1240138;

    public static final int SOUTH_EAST_MASK = 0x1240183, NORTH_EAST_MASK = 0x12401e0;

    public static boolean allow(Entity entity, Entity target) {
        return allow(entity.getX(), entity.getY(), entity.getZ(), entity.getSize(), target.getX(), target.getY(), target.getSize());
    }

    public static boolean allow(Entity entity, Position dest) {
        return allow(entity.getX(), entity.getY(), entity.getZ(), entity.getSize(), dest.getX(), dest.getY(), 1);
    }

    public static boolean allow(Entity entity, int destX, int destY) {
        return allow(entity.getX(), entity.getY(), entity.getZ(), entity.getSize(), destX, destY, 1);
    }

    public static boolean allow(int absX, int absY, int z, int size, int targetX, int targetY, int targetSize) {
        targetX = targetX * 2 + targetSize - 1;
        targetY = targetY * 2 + targetSize - 1;

        absX = absX * 2 + size - 1;
        absY = absY * 2 + size - 1;

        if((targetX & 0x1) != 0)
            targetX += targetX > absX ? -1 : 1;
        if((targetY & 0x1) != 0)
            targetY += targetY > absY ? -1 : 1;

        if((absX & 0x1) != 0)
            absX += absX > targetX ? -1 : 1;
        if((absY & 0x1) != 0)
            absY += absY > targetY ? -1 : 1;

        return allow(absX >> 1, absY >> 1, z, targetX >> 1, targetY >> 1);
    }

    public static boolean allow(int absX, int absY, int z, int destX, int destY) {
        int dx = Math.abs(destX - absX);
        int dy = Math.abs(destY - absY);
        int sx = absX < destX ? 1 : -1;
        int sy = absY < destY ? 1 : -1;
        int err = dx - dy;
        int err2;
        int oldX = absX;
        int oldY = absY;
        while(true) {
            err2 = err << 1;
            if(err2 > -dy) {
                err -= dy;
                absX += sx;
            }
            if(err2 < dx) {
                err += dx;
                absY += sy;
            }
            if(!allowEntrance(oldX, oldY, z, (absX - oldX), (absY - oldY)))
                return false;
            if(absX == destX && absY == destY)
                return true;
            oldX = absX;
            oldY = absY;
        }
    }

    private static boolean allowEntrance(int x, int y, int z, int dx, int dy) {
        if(dx == -1 && dy == 0 && (getClipping(x - 1, y, z) & WEST_MASK) == 0)
            return true;
        if(dx == 1 && dy == 0 && (getClipping(x + 1, y, z) & EAST_MASK) == 0)
            return true;
        if(dx == 0 && dy == -1 && (getClipping(x, y - 1, z) & SOUTH_MASK) == 0)
            return true;
        if(dx == 0 && dy == 1 && (getClipping(x, y + 1, z) & NORTH_MASK) == 0)
            return true;
        if(dx == -1 && dy == -1 && (getClipping(x - 1, y - 1, z) & SOUTH_WEST_MASK) == 0 && (getClipping(x - 1, y, z) & WEST_MASK) == 0 && (getClipping(x, y - 1, z) & SOUTH_MASK) == 0)
            return true;
        if(dx == 1 && dy == -1 && (getClipping(x + 1, y - 1, z) & SOUTH_EAST_MASK) == 0 && (getClipping(x + 1, y, z) & EAST_MASK) == 0 && (getClipping(x, y - 1, z) & SOUTH_MASK) == 0)
            return true;
        if(dx == -1 && dy == 1 && (getClipping(x - 1, y + 1, z) & NORTH_WEST_MASK) == 0 && (getClipping(x - 1, y, z) & WEST_MASK) == 0 && (getClipping(x, y + 1, z) & NORTH_MASK) == 0)
            return true;
        if(dx == 1 && dy == 1 && (getClipping(x + 1, y + 1, z) & NORTH_EAST_MASK) == 0 && (getClipping(x + 1, y, z) & EAST_MASK) == 0 && (getClipping(x, y + 1, z) & NORTH_MASK) == 0)
            return true;
        return false;
    }

    public static int getClipping(Position position) {
        return getClipping(position.getX(), position.getY(), position.getZ());
    }

    public static int getClipping(int x, int y, int z) {
        Position pos = new Position(x, y, z);
        Region region = Region.get(x, y);
        return region.getMask(pos.getZ(), pos.getLocalX(), pos.getLocalY());
    }

}
