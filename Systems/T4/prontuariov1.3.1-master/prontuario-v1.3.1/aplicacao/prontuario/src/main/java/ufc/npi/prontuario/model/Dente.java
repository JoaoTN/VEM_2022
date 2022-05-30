package ufc.npi.prontuario.model;

public enum Dente {

	D18(18), D17(17), D16(16), D15(15), D14(14), D13(13), D12(12), D11(11),
	D21(21), D22(22), D23(23), D24(24), D25(25), D26(26), D27(27), D28(28),
	D48(48), D47(47), D46(46), D45(45), D44(44), D43(43), D42(42), D41(41),
	D31(31), D32(32), D33(33), D34(34), D35(35), D36(36), D37(37), D38(38),
	D55(55), D54(54), D53(53), D52(52), D51(51),
	D61(61), D62(62), D63(63), D64(64), D65(65),
	D85(85), D84(84), D83(83), D82(82), D81(81),
	D71(71), D72(72), D73(73), D74(74), D75(75);

	private Integer numero;

	private Dente(Integer numero) {
		this.numero = numero;
	}

	public Integer getNumero() {
		return numero;
	}
}
