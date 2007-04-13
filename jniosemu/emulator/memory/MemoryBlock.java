package jniosemu.emulator.memory;

import java.util.Vector;
import java.util.HashMap;

/**
 * Contains a part of the memory.
 */
public abstract class MemoryBlock
{
	/**
	 * Name of the part.
	 */
	protected String name;
	/**
	 * External start address.
	 */
	protected int start;
	/**
	 * Length of the memory part.
	 */
	protected int length;
	/**
	 * Contains the memory data
	 */
	protected byte[] memory;

	protected boolean changed = true;

	protected Vector<MemoryInt> memoryVector;

	private HashMap<Integer, MemoryInt.STATE> state = new HashMap<Integer, MemoryInt.STATE>();

	/**
	 * Get the name of the part.
	 *
	 * @calledby MemoryManager.dump()
	 *
	 * @return Name of the part
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the external start address.
	 *
	 * @calledby MemoryManager.mapAddr()
	 *
	 * @return External start address
	 */
	public int getStart() {
		return this.start;
	}

	/**
	 * Get the external start address.
	 *
	 * @calledby MemoryManager.mapAddr()
	 *
	 * @return External end address
	 */
	public int getEnd() {
		return this.start + this.length - 1;
	}

	/**
	 * Get the length of the memory part.
	 *
	 * @return Length of the memory part
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Checks if a memory address exists in this memoryBlock
	 *
	 * @calledby MemoryManager.readByte(), MemoryManager.writeByte()
	 *
	 * @param addr Memory address
	 * @return If the memory address exists in this memoryBlock
	 */
	public boolean inRange(int addr) {
		if (this.start <= addr && this.start + this.length > addr)
			return true;

		return false;
	}

	/**
	 * Map an address to an internal index
	 *
	 * @calledby readByte(), writeByte()
	 *
	 * @param addr Memory address
	 * @return internal index
	 */
	protected int mapAddr(int addr) {
		return addr - this.start;
	}

	/**
	 * Return a byte from a specific memory address
	 *
	 * @calledby MemoryManager.readByte()
	 * @calls mapAddr(), notifyDevice()
	 *
	 * @param addr Memory address
	 * @return Requested byte
	 * @throws MemoryException  If the address don't exits in this memoryBlock
	 */
	public abstract byte readByte(int addr) throws MemoryException;

	/**
	 * Write a byte to memory
	 *
	 * @calledby MemoryManager.writeByte()
	 * @calls mapAddr(), notifyDevice()
	 *
	 * @param addr  Memory address where to place the byte
	 * @param value Byte that should be placed in the memory
	 * @throws MemoryException  If the address don't exits in this memoryBlock
	 */
	public abstract void writeByte(int addr, byte value) throws MemoryException;

	public abstract void reset();

	public abstract boolean resetState();

	public boolean isChanged() {
		return this.changed;
	}

	private void updateMemoryVector() {
		this.memoryVector = new Vector<MemoryInt>();

		for (int i = 0; i < this.length; i += 4) {
			byte[] memoryInt = new byte[4];
			MemoryInt.STATE[] state = new MemoryInt.STATE[4];
			for (int j = 0; j < 4; j++) {
				MemoryInt.STATE tmp = this.state.get(i + j);
				if (tmp == null)
					state[j] = MemoryInt.STATE.UNTOUCHED;
				else
					state[j] = tmp;
			}
			System.arraycopy(this.memory, i, memoryInt, 0, 4);
			this.memoryVector.add(new MemoryInt(this.start+i, memoryInt, state));
		}
	}

	public Vector<MemoryInt> getMemoryVector() {
		if (this.isChanged())
			this.updateMemoryVector();

		return this.memoryVector;
	}

	protected void setState(int index, MemoryInt.STATE state) {
		if (state == MemoryInt.STATE.UNTOUCHED) {
			this.state.remove(index);
		} else {
			this.state.put(index, state);
		}
	}

	protected void clearState() {
		this.state.clear();
	}
}
