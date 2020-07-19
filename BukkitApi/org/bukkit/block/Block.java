package org.bukkit.block;

import java.util.Collection;
import org.bukkit.Chunk;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 代表一个方块.
 * <p>
 * 这是一种动态的对象，在同一个世界的同一个位置只可以存在一个方块.
 * <p>
 * 方块的一个实例可能会根据你对这个方块的一些操作而改变，可以使用block.getState()来获取一个静态的，不会被修改的Block对象.
 * <p>
 * 需要注意的是，在世界生成的过程中调用这个类可能是不安全的，比如BlockPhysicsEvent事件!!!!
 * <p>
 * 原文:
 * Represents a block. This is a live object, and only one Block may exist for
 * any given location in a world. The state of the block may change
 * concurrently to your own handling of it; use block.getState() to get a
 * snapshot state of a block which will not be modified.
 *
 * <br>
 * Note that parts of this class which require access to the world at large
 * (i.e. lighting and power) may not be able to be safely accessed during world
 * generation when used in cases like BlockPhysicsEvent!!!!
 */
public interface Block extends Metadatable {

    /**
     * 获取这个方块的Metadata.
     * <p>
     * 原文:
     * Gets the metadata for this block
     *
     * @return block specific metadata
     * @deprecated Magic value
     */
    @Deprecated
    byte getData();

    /**
     * 获取这个方块的BlockData.
     * <p>
     * 原文:
     * Gets the complete block data for this block
     *
     * @return 方块的BlockData
     */
    @NotNull
    BlockData getBlockData();

    /**
     * 以此方块为基点，在指定的偏移量上获取方块.
     * <p>
     * 原文:
     * Gets the block at the given offsets
     *
     * @param modX X方向的偏移量
     * @param modY Y方向的偏移量
     * @param modZ Z方向的偏移量
     * @return 在此偏移位置的方块
     */
    @NotNull
    Block getRelative(int modX, int modY, int modZ);

    /**
     * 获取这个方块某一面上紧邻的方块.
     * <p>
     * 此方法等同于getRelative(face, 1)
     * <p>
     * 原文:
     * Gets the block at the given face
     * <p>
     * This method is equal to getRelative(face, 1)
     *
     * @param face 哪一面
     * @return 挨着此方块face面的方块
     * @see #getRelative(BlockFace, int)
     */
    @NotNull
    Block getRelative(@NotNull BlockFace face);

    /**
     * 获取这个方块某一面上指定距离的方块.
     * <p>
     * 一个例子，如果我要在一个方块的上面3格放置一个水方块.
     * <pre>
     * Block block = world.getBlockAt(100, 100, 100); //获取到方块
     * Block shower = block.getRelative(BlockFace.UP, 2); //获取这个方块UP(上)面的2格的方块
     * shower.setType(Material.WATER); //设置这个方块为水
     * </pre>
     * 原文:
     * Gets the block at the given distance of the given face
     * <p>
     * For example, the following method places water at 100,102,100; two
     * blocks above 100,100,100.
     *
     * <pre>
     * Block block = world.getBlockAt(100, 100, 100);
     * Block shower = block.getRelative(BlockFace.UP, 2);
     * shower.setType(Material.WATER);
     * </pre>
     *
     * @param face 哪一面
     * @param distance 从face面延伸的距离
     * @return 此方块face面distance距离的方块
     */
    @NotNull
    Block getRelative(@NotNull BlockFace face, int distance);

    /**
     * 获取这个方块的Material.
     * <p>
     * 原文:
     * Gets the type of this block
     *
     * @return 方块的类型
     */
    @NotNull
    Material getType();

    /**
     * 获取这个方块的发光的亮度等级 (0-15).
     * <p>
     * 译注: 如果这个方块不发光则返回0
     * <p>
     * 原文:
     * Gets the light level between 0-15
     *
     * @return 此方块的亮度等级
     */
    byte getLightLevel();

    /**
     * 获取从天空中照到此方块上的光照亮度等级.
     * <p>
     * 会忽略掉来自方块(火把、萤石等)的光源.
     * <p>
     * 原文:
     * Get the amount of light at this block from the sky.
     * <p>
     * Any light given from other sources (such as blocks like torches) will
     * be ignored.
     *
     * @return 来自天空的亮度等级
     */
    byte getLightFromSky();

    /**
     * 获取从方块照到这个方块上的光源亮度等级.
     * <p>
     * 会忽略掉来自非方块(太阳光等)的光源.
     * <p>
     * 原文:
     * Get the amount of light at this block from nearby blocks.
     * <p>
     * Any light given from other sources (such as the sun) will be ignored.
     *
     * @return 来自方块的亮度等级
     */
    byte getLightFromBlocks();

    /**
     * 获取这个方块所在的世界.
     * <p>
     * 原文:
     * Gets the world which contains this Block
     *
     * @return 包含此方块的世界
     */
    @NotNull
    World getWorld();

    /**
     * 获取这个方块的X轴坐标.
     * <p>
     * 原文:
     * Gets the x-coordinate of this block
     *
     * @return X坐标
     */
    int getX();

    /**
     * 获取这个方块的Y轴坐标.
     * <p>
     * 原文:
     * Gets the y-coordinate of this block
     *
     * @return Y坐标
     */
    int getY();

    /**
     * 获取这个方块的Z轴坐标.
     * <p>
     * 原文:
     * Gets the z-coordinate of this block
     *
     * @return z坐标
     */
    int getZ();

    /**
     * 获取这个方块的Location(位置).
     * <p>
     * 原文:
     * Gets the Location of the block
     *
     * @return 此方块的位置
     */
    @NotNull
    Location getLocation();

    /**
     * 将此方块的位置储存在所传入的Location对象中.
     * <p>
     * 如果传入的Location对象为null，则本方法不做任何操作并返回null.
     * <p>
     * 译注: 下面是译者的一个例子
     * <pre>
     * Location loc = new Location(World, 15, 255, 14);
     * Block block = World2.getBlockAt(28, 25, -18);
     * loc = block.getLocation(loc)
     * // 此时，loc的值为(World2, 28, 25, -18) 基本等效于 loc = block.getLocation()
     * </pre>
     * 原文:
     * Stores the location of the block in the provided Location object.
     * <p>
     * If the provided Location is null this method does nothing and returns
     * null.
     *
     * @param loc the location to copy into
     * @return The Location object provided or null
     */
    @Contract("null -> null; !null -> !null")
    @Nullable
    Location getLocation(@Nullable Location loc);

    /**
     * 获取此方块所在的区块.
     * <p>
     * 原文:
     * Gets the chunk which contains this block
     *
     * @return 包含此方块的区块
     */
    @NotNull
    Chunk getChunk();

    /**
     * 设置此方块的BlockData.
     * <p>
     * 原文:
     * Sets the complete data for this block
     *
     * @param data 新的BlockData
     */
    void setBlockData(@NotNull BlockData data);

    /**
     * 设置一个方块的BlockData，并决定是否应用重力.(译者注: 更新方块的意思，如沙不掉落)
     * <br>
     * 请注意，applyPhysics = false 有时并不安全。只有你需要避免周围方块的更新才应该使用这个参数。
     * 例如在创建一个 {@link Bisected} 方块时或者在使用自定义的 BlockPopulator 防止触发无限连锁更新的时候。
     * <p>
     * 不要使用这个方法来在一些 “不可能放置方块的地方” 放置方块。即使可以成功放置，这些方块也会在之后被移除。
     * 如果把大量这种方块放置在很接近的地方可能会使服务器物理引擎过载奔溃。
     * <p>
     * 原文:
     * Sets the complete data for this block
     *
     * <br>
     * Note that applyPhysics = false is not in general safe. It should only be
     * used when you need to avoid triggering a physics update of neighboring
     * blocks, for example when creating a {@link Bisected} block. If you are
     * using a custom populator, then this parameter may also be required to
     * prevent triggering infinite chunk loads on border blocks. This method
     * should NOT be used to "hack" physics by placing blocks in impossible
     * locations. Such blocks are liable to be removed on various events such as
     * world upgrades. Furthermore setting large amounts of such blocks in close
     * proximity may overload the server physics engine if an update is
     * triggered at a later point. If this occurs, the resulting behavior is
     * undefined.
     *
     * @param data 新的BlockData
     * @param applyPhysics 物理系统是否应用在此方块上
     */
    void setBlockData(@NotNull BlockData data, boolean applyPhysics);

    /**
     * 设置这个方块的Material.
     * <p>
     * 原文:
     * Sets the type of this block
     *
     * @param type 新的Material
     */
    void setType(@NotNull Material type);

    /**
     * 设置一个方块的Material，并决定是否应用重力.(译者注: 更新方块的意思，如沙不掉落)
     * <br>
     * 请注意，applyPhysics = false 有时并不安全。只有你需要避免周围方块的更新才应该使用这个参数。
     * 例如在创建一个 {@link Bisected} 方块时或者在使用自定义的 BlockPopulator 防止触发无限连锁更新的时候。
     * <p>
     * 不要使用这个方法来在一些 “不可能放置方块的地方” 放置方块。即使可以成功放置，这些方块也会在之后被移除。
     * 如果把大量这种方块放置在很接近的地方可能会使服务器物理引擎过载奔溃。
     * <p>
     * 原文:
     * Sets the type of this block
     *
     * <br>
     * Note that applyPhysics = false is not in general safe. It should only be
     * used when you need to avoid triggering a physics update of neighboring
     * blocks, for example when creating a {@link Bisected} block. If you are
     * using a custom populator, then this parameter may also be required to
     * prevent triggering infinite chunk loads on border blocks. This method
     * should NOT be used to "hack" physics by placing blocks in impossible
     * locations. Such blocks are liable to be removed on various events such as
     * world upgrades. Furthermore setting large amounts of such blocks in close
     * proximity may overload the server physics engine if an update is
     * triggered at a later point. If this occurs, the resulting behavior is
     * undefined.
     *
     * @param type 新的Material
     * @param applyPhysics 物理系统是否应用在此方块上
     */
    void setType(@NotNull Material type, boolean applyPhysics);

    /**
     * 获取给定的方块在此方块的哪一面.
     * <p>
     * 一个例子:
     * <pre>{@code
     * Block current = world.getBlockAt(100, 100, 100);
     * Block target = world.getBlockAt(100, 101, 100);
     *
     * current.getFace(target) == BlockFace.Up;
     * }</pre>
     * <br>
     * 如果给定的方块不在此方块的旁边，则可能返回null.
     * 原文:
     * Gets the face relation of this block compared to the given block.
     * <p>
     * For example:
     * <pre>{@code
     * Block current = world.getBlockAt(100, 100, 100);
     * Block target = world.getBlockAt(100, 101, 100);
     *
     * current.getFace(target) == BlockFace.Up;
     * }</pre>
     * <br>
     * If the given block is not connected to this block, null may be returned
     *
     * @param block 进行判断的方块
     * @return 给定块挨着的那一面，或者null
     */
    @Nullable
    BlockFace getFace(@NotNull Block block);

    /**
     * 获取此方块的BlockState，然后你可以把这个BlockState转换为其他类型，例如Furnace或Sign。
     * <p>
     * 返回的对象永远不会更新，捕获后的状态可能不会与当前方块的状态一致.
     * <p>
     * 原文:
     * Captures the current state of this block. You may then cast that state
     * into any accepted type, such as Furnace or Sign.
     * <p>
     * The returned object will never be updated, and you are not guaranteed
     * that (for example) a sign is still a sign after you capture its state.
     *
     * @return 具有此方块当前状态的BlockState
     */
    @NotNull
    BlockState getState();

    /**
     * 获取此方块的生物群系.
     * <p>
     * 原文:
     * Returns the biome that this block resides in
     *
     * @return 此方块的生物群系
     */
    @NotNull
    Biome getBiome();

    /**
     * 设置此方块的生物群系.
     * <p>
     * 原文:
     * Sets the biome that this block resides in
     *
     * @param bio 此方块的新生物群系
     */
    void setBiome(@NotNull Biome bio);

    /**
     * 获取此方块是否被红石充能.
     * <p>
     * 原文:
     * Returns true if the block is being powered by Redstone.
     *
     * @return 如果此方块被红石充能，返回true
     */
    boolean isBlockPowered();

    /**
     * 获取此方块是否被红石间接充能.
     * <p>
     * (译者注: 按钮 铁块 活塞 ,按钮按下，铁块被充能，则活塞被间接充能)
     * <p>
     * 原文:
     * Returns true if the block is being indirectly powered by Redstone.
     *
     * @return 如果此方块被间接充能，返回true
     */
    boolean isBlockIndirectlyPowered();

    /**
     * 获取此方块的某一面是否被红石充能.
     * <p>
     * 原文:
     * Returns true if the block face is being powered by Redstone.
     *
     * @param face 哪一面
     * @return 如果这一面被充能，返回true
     */
    boolean isBlockFacePowered(@NotNull BlockFace face);

    /**
     * 获取此方块的某一面是否被红石间接充能.
     * <p>
     * (译者注: 按钮 铁块 活塞 ,按钮按下，铁块被充能，则活塞的左面被间接充能)
     * <p>
     * 原文:
     * Returns true if the block face is being indirectly powered by Redstone.
     *
     * @param face The block face
     * @return True if the block face is indirectly powered.
     */
    boolean isBlockFaceIndirectlyPowered(@NotNull BlockFace face);

    /**
     * 获取此方块某一面的红石充能等级.
     * <p>
     * 原文:
     * Returns the redstone power being provided to this block face
     *
     * @param face the face of the block to query or BlockFace.SELF for the
     *     block itself
     * @return 充能等级
     */
    int getBlockPower(@NotNull BlockFace face);

    /**
     * 获取此方块的红石充能等级.
     * <p>
     * 原文:
     * Returns the redstone power being provided to this block
     *
     * @return 充能等级
     */
    int getBlockPower();

    /**
     * 检查此方块是不是空气.
     * <p>
     * 即{@link #getType()} 返回 {@link Material#AIR}.
     * <p>
     * 原文:
     * Checks if this block is empty.
     * <p>
     * A block is considered empty when {@link #getType()} returns {@link
     * Material#AIR}.
     *
     * @return true if this block is empty
     */
    boolean isEmpty();

    /**
     * 检查此方块是不是流体(水或岩浆等).
     * <p>
     * 即{@link #getType()} 返回 {@link Material#WATER} 或 {@link Material#LAVA}.
     * <p>
     * (译者注: 也有可能是mod流体)
     * <p>
     * 原文:
     * Checks if this block is liquid.
     * <p>
     * A block is considered liquid when {@link #getType()} returns {@link
     * Material#WATER} or {@link Material#LAVA}.
     *
     * @return 如果此方块是流体，返回true
     */
    boolean isLiquid();

    /**
     * Gets the temperature of this block.
     * <p>
     * If the raw biome temperature without adjusting for height effects is
     * required then please use {@link World#getTemperature(int, int)}.
     *
     * @return Temperature of this block
     */
    double getTemperature();

    /**
     * Gets the humidity of the biome of this block
     *
     * @return Humidity of this block
     */
    double getHumidity();

    /**
     * 获取当此方块被活塞移动时的反应.
     * <p>
     * 原文:
     * Returns the reaction of the block when moved by a piston
     *
     * @return 此方块的反应
     */
    @NotNull
    PistonMoveReaction getPistonMoveReaction();

    /**
     * 模拟玩家破坏此方块并掉落物品.
     * <p>
     * 原文:
     * Breaks the block and spawns items as if a player had digged it regardless
     * of the tool.
     *
     * @return 如果此方块被破坏，返回true
     */
    boolean breakNaturally();

    /**
     * 模拟玩家使用某工具破坏此方块并掉落物品.
     * <p>
     * 原文:
     * Breaks the block and spawns items as if a player had digged it with a
     * specific tool
     *
     * @param tool 用来破坏的工具或物品
     * @return 如果此方块被破坏，返回true
     */
    boolean breakNaturally(@Nullable ItemStack tool);

    /**
     * 返回如果被打破，此方块掉落的物品.
     * <p>
     * 原文:
     * Returns a list of items which would drop by destroying this block
     *
     * @return 一个包含掉落物品的Collection
     */
    @NotNull
    Collection<ItemStack> getDrops();

    /**
     * 返回如果被指定的工具打破，此方块掉落的物品.
     * <p>
     * 原文:
     * Returns a list of items which would drop by destroying this block with
     * a specific tool
     *
     * @param tool 用来破坏的工具或物品
     * @return 一个包含掉落物品的Collection
     */
    @NotNull
    Collection<ItemStack> getDrops(@Nullable ItemStack tool);

    /**
     * 返回如果被指定的实体用指定的工具打破，此方块掉落的物品.
     * <p>
     * 原文:
     * Returns a list of items which would drop by the entity destroying this
     * block with a specific tool
     *
     * @param tool The tool or item in hand used for digging
     * @param entity the entity destroying the block
     * @return a list of dropped items for this type of block
     */
    @NotNull
    Collection<ItemStack> getDrops(@NotNull ItemStack tool, @Nullable Entity entity);

    /**
     * 检测玩家能不能穿过此方块.
     * <p>
     * 如果一个方块没有碰撞箱，玩家就可能穿过它.
     * <p>
     * 比如高丛草、花和牌子等。但是打开的门、栅栏门、活板门(地毯等)就不是，因为它们仍然有可以碰撞的部分.
     * <p>
     * 原文:
     * Checks if this block is passable.
     * <p>
     * A block is passable if it has no colliding parts that would prevent
     * players from moving through it.
     * <p>
     * Examples: Tall grass, flowers, signs, etc. are passable, but open doors,
     * fence gates, trap doors, etc. are not because they still have parts that
     * can be collided with.
     *
     * @return <code>true</code> if passable
     */
    boolean isPassable();

    /**
     * Performs a ray trace that checks for collision with this specific block
     * in its current state using its precise collision shape.
     *
     * @param start the start location
     * @param direction the ray direction
     * @param maxDistance the maximum distance
     * @param fluidCollisionMode the fluid collision mode
     * @return the ray trace hit result, or <code>null</code> if there is no hit
     */
    @Nullable
    RayTraceResult rayTrace(@NotNull Location start, @NotNull Vector direction, double maxDistance, @NotNull FluidCollisionMode fluidCollisionMode);

    /**
     * 获取此方块的近似的碰撞箱.
     * <p>
     * 返回的碰撞箱不是完全精确的，因为某些方块({@link org.bukkit.block.data.type.Stairs})的碰撞箱是由多个碰撞箱组合而成的.
     * <p>
     * 此外，返回的碰撞箱可能与实际的碰撞形状不完全相同(例如仙人掌，它具有十六分之十五的碰撞边界)
     * <p>
     * 如果这个方块没有碰撞箱(如空气)则会返回一个空的BoundingBox
     * <p>
     * (译者注: 没有接触过BoundingBox，翻译需要改进)
     * <p>
     * 原文:
     * Gets the approximate bounding box for this block.
     * <p>
     * This isn't exact as some blocks {@link org.bukkit.block.data.type.Stairs}
     * contain many bounding boxes to establish their complete form.
     *
     * Also, the box may not be exactly the same as the collision shape (such as
     * cactus, which is 16/16 of a block with 15/16 collisional bounds).
     *
     * This method will return an empty bounding box if the geometric shape of
     * the block is empty (such as air blocks).
     *
     * @return the approximate bounding box of the block
     */
    @NotNull
    BoundingBox getBoundingBox();
}
