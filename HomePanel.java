package lance;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.Base64;
import java.util.List;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.table.*;
public class HomePanel {
    private JFrame     frame;
    private CardLayout cardLayout;
    private JPanel     mainPanel;
    private String     loggedInUser;
    // ── Cart State ────────────────────────────────────────────────────────────
    private final List<CartItem> cartItems = new ArrayList<>();
    private JLabel cartBadge;
    // ── Per-part image store: SKU → BufferedImage ─────────────────────────────
    private final Map<String, BufferedImage> partImages = new HashMap<>();

    private static final String FILTER_B64 =
        "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";

    static class CartItem {
        String name, sku, price;
        int qty;
        double unitPrice;
        CartItem(String name, String sku, String price, double unitPrice) {
            this.name = name; this.sku = sku; this.price = price;
            this.unitPrice = unitPrice; this.qty = 1;
        }
    }
    // ── All sales records (date, partName, qty, revenue) ─────────────────────
    static class SaleRecord {
        LocalDate date;
        String partName, sku;
        int qty;
        double revenue;
        SaleRecord(String date, String partName, String sku, int qty, double revenue) {
            this.date = LocalDate.parse(date);
            this.partName = partName; this.sku = sku;
            this.qty = qty; this.revenue = revenue;
        }
    }
    private final List<SaleRecord> allSales = buildSampleSales();
    private static List<SaleRecord> buildSampleSales() {
        List<SaleRecord> s = new ArrayList<>();
        s.add(new SaleRecord("2026-01-05","Spark Plug Set",   "IGN-042",10, 1800));
        s.add(new SaleRecord("2026-01-10","Engine Oil Filter","ENG-001",8,  2560));
        s.add(new SaleRecord("2026-01-18","Air Filter",       "AIR-011",6,  1560));
        s.add(new SaleRecord("2026-01-25","Coolant Fluid 1L", "CLT-008",14, 3080));
        s.add(new SaleRecord("2026-02-03","Brake Disc Pad",   "BRK-017",5,  3250));
        s.add(new SaleRecord("2026-02-11","Drive Belt",       "DRV-033",7,  3360));
        s.add(new SaleRecord("2026-02-20","Spark Plug Set",   "IGN-042",12, 2160));
        s.add(new SaleRecord("2026-02-27","Alternator",       "ALT-005",2,  5600));
        s.add(new SaleRecord("2026-03-04","Timing Chain Kit", "TIM-022",4,  5800));
        s.add(new SaleRecord("2026-03-13","Fuel Injector",    "FUL-039",3,  9300));
        s.add(new SaleRecord("2026-03-19","Engine Oil Filter","ENG-001",10, 3200));
        s.add(new SaleRecord("2026-03-28","Air Filter",       "AIR-011",9,  2340));
        s.add(new SaleRecord("2026-04-02","Spark Plug Set",   "IGN-042",14, 2520));
        s.add(new SaleRecord("2026-04-09","Coolant Fluid 1L", "CLT-008",11, 2420));
        s.add(new SaleRecord("2026-04-16","Brake Disc Pad",   "BRK-017",8,  5200));
        s.add(new SaleRecord("2026-04-23","Drive Belt",       "DRV-033",6,  2880));
        s.add(new SaleRecord("2026-04-30","Alternator",       "ALT-005",3,  8400));
        s.add(new SaleRecord("2026-05-05","Timing Chain Kit", "TIM-022",5,  7250));
        s.add(new SaleRecord("2026-05-10","Fuel Injector",    "FUL-039",3,  9300));
        s.add(new SaleRecord("2026-05-14","Spark Plug Set",   "IGN-042",11, 1980));
        s.add(new SaleRecord("2026-05-17","Coolant Fluid 1L", "CLT-008",9,  1980));
        s.add(new SaleRecord("2026-05-20","Engine Oil Filter","ENG-001",8,  2560));
        s.add(new SaleRecord("2026-05-22","Brake Disc Pad",   "BRK-017",4,  2600));
        return s;
    }
    // ── Colors ────────────────────────────────────────────────────────────────
    static final Color BG       = new Color(26, 26, 26);
    static final Color SIDEBAR  = new Color(17, 17, 17);
    static final Color TOPBAR   = new Color(34, 34, 34);
    static final Color CARD     = new Color(34, 34, 34);
    static final Color BORDER   = new Color(42, 42, 42);
    static final Color ORANGE   = new Color(255, 140, 0);
    static final Color TEXT     = new Color(221, 221, 221);
    static final Color MUTED    = new Color(100, 100, 100);
    static final Color GREEN_BG = new Color(13, 42, 21);
    static final Color GREEN_FG = new Color(76, 175, 118);
    static final Color WARN_BG  = new Color(42, 26, 13);
    static final Color WARN_FG  = new Color(224, 128, 64);
    static final Color RED_BG   = new Color(42, 13, 13);
    static final Color RED_FG   = new Color(220, 80, 80);
    static final Color BLUE_BG  = new Color(13, 26, 42);
    static final Color BLUE_FG  = new Color(80, 160, 220);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HomePanel("admin").showWindow());
    }
    public HomePanel(String username) {
        this.loggedInUser = username;
        initialize();
    }
    private void initialize() {
        frame = new JFrame("MotorShop Ordering System");
        frame.setSize(960, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.setBackground(BG);
        mainPanel.add(buildDashboard(),      "dashboard");
        mainPanel.add(buildPartsPanel(),     "parts");
        mainPanel.add(buildOrdersPanel(),    "orders");
        mainPanel.add(buildCustomersPanel(), "customers");
        mainPanel.add(buildReportsPanel(),   "reports");
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        cardLayout.show(mainPanel, "dashboard");
        loadDefaultImages();
    }
    public void showWindow() { frame.setVisible(true); }
    private void doLogout() {
        int c = JOptionPane.showConfirmDialog(frame,
            "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) {
            frame.dispose();
            SwingUtilities.invokeLater(() -> new Login().frame.setVisible(true));
        }
    }
    // ═════════════════════════════════════════════════════════════════════════
    // CART LOGIC
    // ═════════════════════════════════════════════════════════════════════════
    private void addToCart(String name, String sku, String price, double unitPrice) {
        for (CartItem item : cartItems) {
            if (item.sku.equals(sku)) { item.qty++; updateCartBadge(); showCartToast(name + " qty updated to " + item.qty); return; }
        }
        cartItems.add(new CartItem(name, sku, price, unitPrice));
        updateCartBadge();
        showCartToast(name + " added to cart!");
    }
    private void updateCartBadge() {
        if (cartBadge == null) return;
        int total = cartItems.stream().mapToInt(i -> i.qty).sum();
        cartBadge.setText(String.valueOf(total));
        cartBadge.setVisible(total > 0);
    }
    private void showCartToast(String msg) {
        JWindow toast = new JWindow(frame);
        JLabel lbl = new JLabel("  🛒  " + msg + "  ");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        lbl.setForeground(new Color(30, 15, 0));
        lbl.setBackground(ORANGE); lbl.setOpaque(true);
        lbl.setBorder(new EmptyBorder(8, 12, 8, 12));
        toast.getContentPane().add(lbl); toast.pack();
        Point loc = frame.getLocationOnScreen();
        toast.setLocation(loc.x + frame.getWidth() - toast.getWidth() - 20,
                          loc.y + frame.getHeight() - toast.getHeight() - 40);
        toast.setVisible(true);
        new Timer(2000, ev -> toast.dispose()).start();
    }
    private void showCartDialog() {
        JDialog dialog = new JDialog(frame, "🛒  Shopping Cart", true);
        dialog.setSize(560, 520); dialog.setLocationRelativeTo(frame);
        dialog.getContentPane().setBackground(BG); dialog.setLayout(new BorderLayout());
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(TOPBAR);
        titleBar.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0,BORDER), new EmptyBorder(12,20,12,20)));
        JLabel titleLbl = new JLabel("🛒  Cart  —  " + cartItems.stream().mapToInt(i->i.qty).sum() + " item(s)");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 15)); titleLbl.setForeground(TEXT);
        titleBar.add(titleLbl, BorderLayout.WEST); dialog.add(titleBar, BorderLayout.NORTH);
        JPanel itemsPanel = new JPanel();
        itemsPanel.setBackground(BG); itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        itemsPanel.setBorder(new EmptyBorder(12, 16, 12, 16));
        Runnable[] refreshCart = {null};
        JLabel[] totalLabel = {new JLabel()};
        totalLabel[0].setFont(new Font("SansSerif", Font.BOLD, 18)); totalLabel[0].setForeground(ORANGE);
        refreshCart[0] = () -> {
            itemsPanel.removeAll();
            if (cartItems.isEmpty()) {
                JLabel empty = new JLabel("Your cart is empty.");
                empty.setFont(new Font("SansSerif", Font.PLAIN, 13)); empty.setForeground(MUTED);
                empty.setAlignmentX(Component.LEFT_ALIGNMENT); empty.setBorder(new EmptyBorder(20,0,0,0));
                itemsPanel.add(empty);
            } else {
                for (CartItem item : cartItems) { itemsPanel.add(buildCartRow(item, refreshCart, totalLabel, dialog)); itemsPanel.add(Box.createVerticalStrut(6)); }
            }
            double total = cartItems.stream().mapToDouble(i -> i.unitPrice * i.qty).sum();
            totalLabel[0].setText(formatPeso(total));
            itemsPanel.revalidate(); itemsPanel.repaint();
            titleLbl.setText("🛒  Cart  —  " + cartItems.stream().mapToInt(i->i.qty).sum() + " item(s)");
        };
        refreshCart[0].run();
        JScrollPane scroll = new JScrollPane(itemsPanel);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false); scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        dialog.add(scroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(TOPBAR);
        footer.setBorder(new CompoundBorder(new MatteBorder(1,0,0,0,BORDER), new EmptyBorder(14,20,14,20)));
        JPanel totRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        totRow.setBackground(TOPBAR);
        JLabel totLbl = new JLabel("Order Total:"); totLbl.setFont(new Font("SansSerif", Font.PLAIN, 13)); totLbl.setForeground(MUTED);
        double init = cartItems.stream().mapToDouble(i -> i.unitPrice * i.qty).sum();
        totalLabel[0].setText(formatPeso(init));
        totRow.add(totLbl); totRow.add(totalLabel[0]); footer.add(totRow, BorderLayout.WEST);
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); btnRow.setBackground(TOPBAR);
        JButton clearBtn = makeDarkButton("Clear Cart");
        clearBtn.addActionListener(e -> { cartItems.clear(); updateCartBadge(); refreshCart[0].run(); });
        JButton placeBtn = makeOrangeButton("Place Order  →");
        placeBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        placeBtn.addActionListener(e -> { if (cartItems.isEmpty()) { JOptionPane.showMessageDialog(dialog, "Your cart is empty!", "Cannot Place Order", JOptionPane.WARNING_MESSAGE); return; } dialog.dispose(); showPlaceOrderDialog(); });
        btnRow.add(clearBtn); btnRow.add(placeBtn); footer.add(btnRow, BorderLayout.EAST);
        dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    private JPanel buildCartRow(CartItem item, Runnable[] refreshCart, JLabel[] totalLabel, JDialog dialog) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD);
        row.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(10,14,10,14)));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70)); row.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel info = new JPanel(); info.setBackground(CARD); info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        JLabel nameLbl = new JLabel(item.name); nameLbl.setFont(new Font("SansSerif", Font.BOLD, 13)); nameLbl.setForeground(TEXT);
        JLabel skuLbl = new JLabel(item.sku + "  ·  " + item.price + " each"); skuLbl.setFont(new Font("SansSerif", Font.PLAIN, 11)); skuLbl.setForeground(MUTED);
        info.add(nameLbl); info.add(Box.createVerticalStrut(3)); info.add(skuLbl); row.add(info, BorderLayout.WEST);
        JPanel qtyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0)); qtyPanel.setBackground(CARD);
        JButton minus = makeQtyBtn("−"); JLabel qtyLbl = new JLabel(String.valueOf(item.qty));
        qtyLbl.setFont(new Font("SansSerif", Font.BOLD, 14)); qtyLbl.setForeground(TEXT);
        qtyLbl.setPreferredSize(new Dimension(28, 24)); qtyLbl.setHorizontalAlignment(SwingConstants.CENTER);
        JButton plus = makeQtyBtn("+");
        minus.addActionListener(e -> { if (item.qty > 1) { item.qty--; } else { cartItems.remove(item); updateCartBadge(); } refreshCart[0].run(); });
        plus.addActionListener(e -> { item.qty++; refreshCart[0].run(); });
        qtyPanel.add(minus); qtyPanel.add(qtyLbl); qtyPanel.add(plus); row.add(qtyPanel, BorderLayout.CENTER);
        JPanel right = new JPanel(new BorderLayout()); right.setBackground(CARD);
        JLabel subLbl = new JLabel(formatPeso(item.unitPrice * item.qty));
        subLbl.setFont(new Font("SansSerif", Font.BOLD, 14)); subLbl.setForeground(ORANGE); subLbl.setHorizontalAlignment(SwingConstants.RIGHT);
        JButton removeBtn = new JButton("✕"); removeBtn.setFont(new Font("SansSerif", Font.BOLD, 10)); removeBtn.setForeground(MUTED);
        removeBtn.setBackground(CARD); removeBtn.setBorderPainted(false); removeBtn.setFocusPainted(false);
        removeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        removeBtn.addActionListener(e -> { cartItems.remove(item); updateCartBadge(); refreshCart[0].run(); });
        right.add(subLbl, BorderLayout.NORTH); right.add(removeBtn, BorderLayout.SOUTH); row.add(right, BorderLayout.EAST);
        return row;
    }
    private JButton makeQtyBtn(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14)); btn.setForeground(TEXT);
        btn.setBackground(new Color(50,50,50)); btn.setPreferredSize(new Dimension(28,24));
        btn.setBorder(new LineBorder(BORDER,1)); btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(255,140,0,60)); btn.setBorder(new LineBorder(ORANGE,1)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(50,50,50)); btn.setBorder(new LineBorder(BORDER,1)); }
        });
        return btn;
    }
    private void showPlaceOrderDialog() {
        JDialog dialog = new JDialog(frame, "Place Order", true);
        dialog.setSize(480, 500); dialog.setLocationRelativeTo(frame);
        dialog.getContentPane().setBackground(BG); dialog.setLayout(new BorderLayout());
        JPanel header = new JPanel(new BorderLayout()); header.setBackground(TOPBAR);
        header.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0,BORDER), new EmptyBorder(14,20,14,20)));
        JLabel title = new JLabel("Confirm Your Order"); title.setFont(new Font("SansSerif", Font.BOLD, 16)); title.setForeground(TEXT);
        header.add(title, BorderLayout.WEST); dialog.add(header, BorderLayout.NORTH);
        JPanel body = new JPanel(); body.setBackground(BG); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBorder(new EmptyBorder(20,20,20,20));
        body.add(buildFormLabel("Customer Name")); body.add(Box.createVerticalStrut(6));
        JTextField custField = buildFormField("Enter customer name"); body.add(custField); body.add(Box.createVerticalStrut(16));
        body.add(buildFormLabel("Order Note (optional)")); body.add(Box.createVerticalStrut(6));
        JTextArea noteArea = new JTextArea(3,20); noteArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        noteArea.setForeground(TEXT); noteArea.setBackground(CARD); noteArea.setCaretColor(ORANGE);
        noteArea.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(8,10,8,10)));
        noteArea.setLineWrap(true); noteArea.setWrapStyleWord(true);
        JScrollPane noteScroll = new JScrollPane(noteArea); noteScroll.setBorder(null);
        noteScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); noteScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(noteScroll); body.add(Box.createVerticalStrut(20));
        body.add(buildFormLabel("Order Summary")); body.add(Box.createVerticalStrut(8));
        JPanel summaryPanel = new JPanel(); summaryPanel.setBackground(CARD); summaryPanel.setLayout(new BoxLayout(summaryPanel, BoxLayout.Y_AXIS));
        summaryPanel.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(10,14,10,14)));
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT); summaryPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        double grandTotal = 0;
        for (CartItem item : cartItems) {
            JPanel line = new JPanel(new BorderLayout()); line.setBackground(CARD); line.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
            JLabel left = new JLabel(item.name + "  ×" + item.qty); left.setFont(new Font("SansSerif", Font.PLAIN, 12)); left.setForeground(TEXT);
            double sub = item.unitPrice * item.qty; grandTotal += sub;
            JLabel right = new JLabel(formatPeso(sub)); right.setFont(new Font("SansSerif", Font.BOLD, 12)); right.setForeground(ORANGE);
            line.add(left, BorderLayout.WEST); line.add(right, BorderLayout.EAST);
            summaryPanel.add(line); summaryPanel.add(Box.createVerticalStrut(4));
        }
        summaryPanel.add(Box.createVerticalStrut(6));
        JSeparator sep = new JSeparator(); sep.setForeground(BORDER); sep.setBackground(BORDER); sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        summaryPanel.add(sep); summaryPanel.add(Box.createVerticalStrut(8));
        JPanel totalRow = new JPanel(new BorderLayout()); totalRow.setBackground(CARD); totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        JLabel totLbl = new JLabel("TOTAL"); totLbl.setFont(new Font("SansSerif", Font.BOLD, 12)); totLbl.setForeground(MUTED);
        JLabel totVal = new JLabel(formatPeso(grandTotal)); totVal.setFont(new Font("SansSerif", Font.BOLD, 20)); totVal.setForeground(ORANGE);
        totalRow.add(totLbl, BorderLayout.WEST); totalRow.add(totVal, BorderLayout.EAST); summaryPanel.add(totalRow); body.add(summaryPanel);
        JScrollPane bodyScroll = new JScrollPane(body); bodyScroll.setOpaque(false); bodyScroll.getViewport().setOpaque(false); bodyScroll.setBorder(null);
        dialog.add(bodyScroll, BorderLayout.CENTER);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12)); footer.setBackground(TOPBAR); footer.setBorder(new MatteBorder(1,0,0,0,BORDER));
        JButton cancelBtn = makeDarkButton("Cancel"); cancelBtn.addActionListener(e -> dialog.dispose());
        double finalGrandTotal = grandTotal;
        JButton confirmBtn = makeOrangeButton("✔  Confirm & Place Order"); confirmBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        confirmBtn.addActionListener(e -> {
            String cust = custField.getText().trim();
            if (cust.isEmpty()) { custField.setBorder(new CompoundBorder(new LineBorder(RED_FG,1), new EmptyBorder(6,10,6,10))); custField.requestFocus(); return; }
            dialog.dispose(); showOrderSuccessDialog(cust, finalGrandTotal); cartItems.clear(); updateCartBadge();
        });
        footer.add(cancelBtn); footer.add(confirmBtn); dialog.add(footer, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    private void showOrderSuccessDialog(String customer, double total) {
        JDialog d = new JDialog(frame, "Order Placed!", true);
        d.setSize(360, 240); d.setLocationRelativeTo(frame);
        d.getContentPane().setBackground(BG); d.setLayout(new BoxLayout(d.getContentPane(), BoxLayout.Y_AXIS));
        JPanel pad = new JPanel(); pad.setBackground(BG); pad.setLayout(new BoxLayout(pad, BoxLayout.Y_AXIS)); pad.setBorder(new EmptyBorder(30,30,20,30));
        JLabel icon = new JLabel("✅"); icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36)); icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel msg = new JLabel("Order placed successfully!"); msg.setFont(new Font("SansSerif", Font.BOLD, 15)); msg.setForeground(GREEN_FG); msg.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel sub = new JLabel("Customer: " + customer + "  |  Total: " + formatPeso(total)); sub.setFont(new Font("SansSerif", Font.PLAIN, 11)); sub.setForeground(MUTED); sub.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton ok = makeOrangeButton("Done"); ok.setAlignmentX(Component.CENTER_ALIGNMENT); ok.addActionListener(e -> d.dispose());
        pad.add(icon); pad.add(Box.createVerticalStrut(10)); pad.add(msg); pad.add(Box.createVerticalStrut(6)); pad.add(sub); pad.add(Box.createVerticalStrut(20)); pad.add(ok);
        d.add(pad); d.setVisible(true);
    }
    private JLabel buildFormLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase()); lbl.setFont(new Font("SansSerif", Font.BOLD, 10)); lbl.setForeground(MUTED); lbl.setAlignmentX(Component.LEFT_ALIGNMENT); return lbl;
    }
    private JTextField buildFormField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13)); tf.setForeground(TEXT); tf.setBackground(CARD); tf.setCaretColor(ORANGE);
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(6,10,6,10)));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36)); tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        tf.setForeground(MUTED); tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT); } tf.setBorder(new CompoundBorder(new LineBorder(ORANGE,1), new EmptyBorder(6,10,6,10))); }
            public void focusLost(FocusEvent e)   { if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(MUTED); } tf.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(6,10,6,10))); }
        });
        return tf;
    }
    private String formatPeso(double amount) { return String.format("₱%,.2f", amount); }

    // ═════════════════════════════════════════════════════════════════════════
    // IMAGE UPLOAD LOGIC
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * Decodes the embedded Base64 images and pre-loads them into partImages
     * so they appear permanently on every launch without any file on disk.
     */
    private void loadDefaultImages() {
        try {
            byte[] bytes = Base64.getDecoder().decode(FILTER_B64);
            BufferedImage raw = ImageIO.read(new ByteArrayInputStream(bytes));
            if (raw != null) {
                int tw = 100, th = 90;
                double scale = Math.max((double) tw / raw.getWidth(), (double) th / raw.getHeight());
                int sw = (int)(raw.getWidth()  * scale);
                int sh = (int)(raw.getHeight() * scale);
                Image scaled = raw.getScaledInstance(sw, sh, Image.SCALE_SMOOTH);
                BufferedImage thumb = new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = thumb.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(scaled, (tw - sw) / 2, (th - sh) / 2, null);
                g2.dispose();
                partImages.put("ENG-001", thumb);
            }
        } catch (Exception ex) {
            // If decoding fails for any reason, the placeholder will show instead
            System.err.println("Warning: could not load default image for ENG-001: " + ex.getMessage());
        }
    }

    /**
     * Opens a file chooser, loads the selected image, scales it to fit the
     * thumbnail area, stores it under the part's SKU, and repaints the
     * provided image panel so the card updates immediately.
     */
    private void handleImageUpload(String sku, JPanel imagePanel) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Part Image");
        chooser.setFileFilter(new FileNameExtensionFilter(
            "Image Files (JPG, PNG, GIF, BMP, WEBP)", "jpg", "jpeg", "png", "gif", "bmp", "webp"));
        chooser.setAcceptAllFileFilterUsed(false);

        // Apply dark look to the chooser
        UIManager.put("FileChooser.background", CARD);
        UIManager.put("FileChooser.foreground", TEXT);

        int result = chooser.showOpenDialog(frame);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = chooser.getSelectedFile();
        try {
            BufferedImage raw = ImageIO.read(file);
            if (raw == null) {
                JOptionPane.showMessageDialog(frame,
                    "Could not read image file.\nPlease choose a valid JPG, PNG, or GIF.",
                    "Image Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Scale to fill the thumbnail (100 × 90) while keeping aspect ratio
            int tw = 100, th = 90;
            double scale = Math.max((double) tw / raw.getWidth(), (double) th / raw.getHeight());
            int sw = (int)(raw.getWidth()  * scale);
            int sh = (int)(raw.getHeight() * scale);
            Image scaled = raw.getScaledInstance(sw, sh, Image.SCALE_SMOOTH);
            BufferedImage thumb = new BufferedImage(tw, th, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = thumb.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(scaled, (tw - sw) / 2, (th - sh) / 2, null);
            g2.dispose();

            partImages.put(sku, thumb);
            imagePanel.repaint();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame,
                "Failed to load image: " + ex.getMessage(),
                "Image Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Builds a square image-preview + upload-button panel for a part card.
     * Width is fixed at 100 px; height at 90 px for the preview area.
     */
    private JPanel buildImageSlot(String sku) {
        // Custom painting panel that shows the thumbnail or a placeholder
        JPanel preview = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                // Background
                g2.setColor(new Color(22, 22, 22));
                g2.fillRoundRect(0, 0, w, h, 8, 8);

                BufferedImage img = partImages.get(sku);
                if (img != null) {
                    // Draw stored thumbnail centred
                    int ix = (w - img.getWidth())  / 2;
                    int iy = (h - img.getHeight()) / 2;
                    g2.drawImage(img, ix, iy, null);
                } else {
                    // Placeholder: dashed border + camera icon + hint text
                    float[] dash = {4f, 4f};
                    g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER, 10f, dash, 0f));
                    g2.setColor(new Color(70, 70, 70));
                    g2.drawRoundRect(2, 2, w - 4, h - 4, 8, 8);
                    g2.setStroke(new BasicStroke(1f));

                    // Camera icon (simple SVG-style lines)
                    int cx = w / 2, cy = h / 2 - 8;
                    g2.setColor(new Color(80, 80, 80));
                    // Body of camera
                    g2.drawRoundRect(cx - 14, cy - 8, 28, 20, 4, 4);
                    // Lens circle
                    g2.drawOval(cx - 6, cy - 5, 12, 12);
                    // Viewfinder bump
                    g2.drawRoundRect(cx - 5, cy - 12, 10, 5, 3, 3);
                    // Flash dot
                    g2.fillOval(cx + 8, cy - 8, 3, 3);

                    g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    g2.setColor(new Color(90, 90, 90));
                    String hint = "Click to upload";
                    int hw = g2.getFontMetrics().stringWidth(hint);
                    g2.drawString(hint, (w - hw) / 2, cy + 22);
                }
            }
        };
        preview.setPreferredSize(new Dimension(100, 90));
        preview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        preview.setBackground(new Color(22, 22, 22));
        preview.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        preview.setToolTipText("Click to upload a photo for this part");

        // Hover effect: lighten border on mouse-over
        preview.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { preview.setBorder(new LineBorder(ORANGE, 1, true)); }
            @Override public void mouseExited (MouseEvent e) { preview.setBorder(null); }
            @Override public void mouseClicked(MouseEvent e) { handleImageUpload(sku, preview); }
        });

        // Small "Change Photo" button below the preview
        JButton changeBtn = new JButton("📷  Photo");
        changeBtn.setFont(new Font("SansSerif", Font.PLAIN, 10));
        changeBtn.setForeground(MUTED);
        changeBtn.setBackground(new Color(28, 28, 28));
        changeBtn.setBorder(new LineBorder(BORDER, 1));
        changeBtn.setFocusPainted(false);
        changeBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changeBtn.setPreferredSize(new Dimension(100, 22));
        changeBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        changeBtn.addActionListener(e -> handleImageUpload(sku, preview));
        changeBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { changeBtn.setForeground(ORANGE); changeBtn.setBorder(new LineBorder(ORANGE, 1)); }
            @Override public void mouseExited (MouseEvent e) { changeBtn.setForeground(MUTED);  changeBtn.setBorder(new LineBorder(BORDER, 1)); }
        });

        // Wrapper stacks preview + button vertically
        JPanel slot = new JPanel();
        slot.setLayout(new BoxLayout(slot, BoxLayout.Y_AXIS));
        slot.setBackground(CARD);
        slot.add(preview);
        slot.add(Box.createVerticalStrut(4));
        slot.add(changeBtn);
        return slot;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PANELS
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildDashboard() {
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(BG);
        root.add(buildSidebar("dashboard"), BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout()); content.setBackground(BG);
        content.add(buildTopBar("Dashboard"), BorderLayout.NORTH);
        JPanel body = new JPanel(); body.setBackground(BG); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBorder(new EmptyBorder(24,24,24,24));
        JLabel welcome = new JLabel("Welcome back, " + loggedInUser + " \uD83D\uDC4B"); welcome.setFont(new Font("SansSerif", Font.BOLD, 18)); welcome.setForeground(TEXT); welcome.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(welcome); body.add(Box.createVerticalStrut(4));
        JLabel sub = new JLabel("Here's what's happening at MotorShop today."); sub.setFont(new Font("SansSerif", Font.PLAIN, 12)); sub.setForeground(MUTED); sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(sub); body.add(Box.createVerticalStrut(20));
        body.add(buildSectionLabel("Overview")); body.add(Box.createVerticalStrut(10));
        body.add(buildStatsRow()); body.add(Box.createVerticalStrut(28));
        body.add(buildSectionLabel("Quick Navigation")); body.add(Box.createVerticalStrut(10));
        JPanel navRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0)); navRow.setBackground(BG); navRow.setAlignmentX(Component.LEFT_ALIGNMENT); navRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        JButton toParts = makeNavCard("Parts Inventory","View & manage parts"); toParts.addActionListener(e -> cardLayout.show(mainPanel,"parts"));
        JButton toOrders = makeNavCard("Orders","Track orders"); toOrders.addActionListener(e -> cardLayout.show(mainPanel,"orders"));
        JButton toCust = makeNavCard("Customers","Customer records"); toCust.addActionListener(e -> cardLayout.show(mainPanel,"customers"));
        JButton toRep = makeNavCard("Reports","Sales & analytics"); toRep.addActionListener(e -> cardLayout.show(mainPanel,"reports"));
        navRow.add(toParts); navRow.add(toOrders); navRow.add(toCust); navRow.add(toRep); body.add(navRow);
        JScrollPane scroll = new JScrollPane(body); scroll.setOpaque(false); scroll.getViewport().setOpaque(false); scroll.setBorder(null); scroll.getVerticalScrollBar().setUnitIncrement(14);
        content.add(scroll, BorderLayout.CENTER); root.add(content, BorderLayout.CENTER); return root;
    }
    private JPanel buildPartsPanel() {
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(BG);
        root.add(buildSidebar("parts"), BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout()); content.setBackground(BG);
        content.add(buildTopBar("Parts Inventory"), BorderLayout.NORTH);
        JPanel body = new JPanel(); body.setBackground(BG); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBorder(new EmptyBorder(24,24,24,24));
        body.add(buildSectionLabel("Stats")); body.add(Box.createVerticalStrut(10)); body.add(buildStatsRow()); body.add(Box.createVerticalStrut(28));
        body.add(buildSectionLabel("Motor Parts Catalog")); body.add(Box.createVerticalStrut(10)); body.add(buildPartsGrid());
        JScrollPane scroll = new JScrollPane(body); scroll.setOpaque(false); scroll.getViewport().setOpaque(false); scroll.setBorder(null); scroll.getVerticalScrollBar().setUnitIncrement(14);
        content.add(scroll, BorderLayout.CENTER); root.add(content, BorderLayout.CENTER); return root;
    }
    private JPanel buildOrdersPanel() {
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(BG);
        root.add(buildSidebar("orders"), BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout()); content.setBackground(BG);
        content.add(buildTopBar("Orders"), BorderLayout.NORTH);
        JPanel body = new JPanel(); body.setBackground(BG); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBorder(new EmptyBorder(24,24,24,24));
        JPanel stats = new JPanel(new GridLayout(1,4,10,0)); stats.setBackground(BG); stats.setMaximumSize(new Dimension(Integer.MAX_VALUE,76)); stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(buildStatCard("Total Orders","134",TEXT)); stats.add(buildStatCard("Pending","18",WARN_FG)); stats.add(buildStatCard("Completed","110",GREEN_FG)); stats.add(buildStatCard("Cancelled","6",RED_FG));
        body.add(buildSectionLabel("Summary")); body.add(Box.createVerticalStrut(10)); body.add(stats); body.add(Box.createVerticalStrut(28));
        body.add(buildSectionLabel("Recent Orders")); body.add(Box.createVerticalStrut(10));
        String[] cols = {"Order ID","Customer","Parts","Total","Date","Status"};
        Object[][] data = {{"ORD-001","Juan dela Cruz","Spark Plug x2","₱360","2026-05-20","Completed"},{"ORD-002","Maria Santos","Brake Disc Pad x1","₱650","2026-05-20","Pending"},{"ORD-003","Pedro Reyes","Engine Oil Filter x3","₱960","2026-05-19","Completed"},{"ORD-004","Ana Gonzales","Drive Belt x1","₱480","2026-05-19","Pending"},{"ORD-005","Carlos Lim","Alternator x1","₱2,800","2026-05-18","Completed"},{"ORD-006","Rosa Villanueva","Air Filter x2","₱520","2026-05-18","Cancelled"},{"ORD-007","Jose Bautista","Coolant Fluid x4","₱880","2026-05-17","Completed"},{"ORD-008","Linda Morales","Fuel Injector x1","₱3,100","2026-05-17","Pending"}};
        body.add(buildStyledTable(cols, data));
        JScrollPane scroll = new JScrollPane(body); scroll.setOpaque(false); scroll.getViewport().setOpaque(false); scroll.setBorder(null); scroll.getVerticalScrollBar().setUnitIncrement(14);
        content.add(scroll, BorderLayout.CENTER); root.add(content, BorderLayout.CENTER); return root;
    }
    private JPanel buildCustomersPanel() {
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(BG);
        root.add(buildSidebar("customers"), BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout()); content.setBackground(BG);
        content.add(buildTopBar("Customers"), BorderLayout.NORTH);
        JPanel body = new JPanel(); body.setBackground(BG); body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS)); body.setBorder(new EmptyBorder(24,24,24,24));
        JPanel stats = new JPanel(new GridLayout(1,3,10,0)); stats.setBackground(BG); stats.setMaximumSize(new Dimension(Integer.MAX_VALUE,76)); stats.setAlignmentX(Component.LEFT_ALIGNMENT);
        stats.add(buildStatCard("Total Customers","52",TEXT)); stats.add(buildStatCard("Active","44",GREEN_FG)); stats.add(buildStatCard("New This Month","7",BLUE_FG));
        body.add(buildSectionLabel("Summary")); body.add(Box.createVerticalStrut(10)); body.add(stats); body.add(Box.createVerticalStrut(28));
        body.add(buildSectionLabel("Customer List")); body.add(Box.createVerticalStrut(10));
        String[] cols = {"Customer ID","Name","Phone","Total Orders","Last Order","Status"};
        Object[][] data = {{"CUST-001","Juan dela Cruz","09171234567","12","2026-05-20","Active"},{"CUST-002","Maria Santos","09281234567","8","2026-05-20","Active"},{"CUST-003","Pedro Reyes","09391234567","5","2026-05-19","Active"},{"CUST-004","Ana Gonzales","09501234567","3","2026-05-19","Active"},{"CUST-005","Carlos Lim","09611234567","20","2026-05-18","Active"},{"CUST-006","Rosa Villanueva","09721234567","1","2026-05-18","Inactive"},{"CUST-007","Jose Bautista","09831234567","9","2026-05-17","Active"},{"CUST-008","Linda Morales","09941234567","4","2026-05-17","Active"}};
        body.add(buildStyledTable(cols, data));
        JScrollPane scroll = new JScrollPane(body); scroll.setOpaque(false); scroll.getViewport().setOpaque(false); scroll.setBorder(null); scroll.getVerticalScrollBar().setUnitIncrement(14);
        content.add(scroll, BorderLayout.CENTER); root.add(content, BorderLayout.CENTER); return root;
    }
    // ═════════════════════════════════════════════════════════════════════════
    // REPORTS PANEL
    // ═════════════════════════════════════════════════════════════════════════
    private JPanel buildReportsPanel() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildSidebar("reports"), BorderLayout.WEST);
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BG);
        content.add(buildTopBar("Reports"), BorderLayout.NORTH);
        JPanel body = new JPanel();
        body.setBackground(BG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(24, 24, 24, 24));
        body.add(buildSectionLabel("Date Range Filter"));
        body.add(Box.createVerticalStrut(10));
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterBar.setBackground(BG);
        filterBar.setAlignmentX(Component.LEFT_ALIGNMENT);
        filterBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        JLabel fromLbl = new JLabel("From:");
        fromLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        fromLbl.setForeground(MUTED);
        JTextField fromField = buildDateField("2026-01-01");
        JLabel toLbl = new JLabel("To:");
        toLbl.setFont(new Font("SansSerif", Font.BOLD, 12));
        toLbl.setForeground(MUTED);
        JTextField toField = buildDateField("2026-05-22");
        JButton applyBtn = makeOrangeButton("Apply Filter");
        applyBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        JButton resetBtn = makeDarkButton("Reset");
        resetBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        JLabel filterError = new JLabel("");
        filterError.setFont(new Font("SansSerif", Font.PLAIN, 11));
        filterError.setForeground(RED_FG);
        filterBar.add(fromLbl); filterBar.add(fromField);
        filterBar.add(toLbl);   filterBar.add(toField);
        filterBar.add(applyBtn); filterBar.add(resetBtn);
        filterBar.add(filterError);
        body.add(filterBar);
        body.add(Box.createVerticalStrut(24));
        JPanel sections = new JPanel();
        sections.setBackground(BG);
        sections.setLayout(new BoxLayout(sections, BoxLayout.Y_AXIS));
        sections.setAlignmentX(Component.LEFT_ALIGNMENT);
        Runnable[] rebuild = {null};
        LocalDate[] rangeRef = { LocalDate.parse("2026-01-01"), LocalDate.parse("2026-05-22") };
        rebuild[0] = () -> {
            LocalDate start = rangeRef[0];
            LocalDate end   = rangeRef[1];
            List<SaleRecord> filtered = new ArrayList<>();
            for (SaleRecord r : allSales)
                if (!r.date.isBefore(start) && !r.date.isAfter(end)) filtered.add(r);
            double totalRev  = filtered.stream().mapToDouble(r -> r.revenue).sum();
            int    totalQty  = filtered.stream().mapToInt(r -> r.qty).sum();
            long   totalDays = start.until(end).getDays() + 1;
            double dailyAvg  = totalDays > 0 ? totalRev / totalDays : 0;
            Map<String, Double> byMonth = new LinkedHashMap<>();
            for (SaleRecord r : filtered) {
                String key = r.date.format(DateTimeFormatter.ofPattern("yyyy-MM"));
                byMonth.merge(key, r.revenue, Double::sum);
            }
            Map<String, Double> byYear = new LinkedHashMap<>();
            for (SaleRecord r : filtered) {
                String key = String.valueOf(r.date.getYear());
                byYear.merge(key, r.revenue, Double::sum);
            }
            Map<String, Double> byDay = new LinkedHashMap<>();
            for (SaleRecord r : filtered) {
                String key = r.date.toString();
                byDay.merge(key, r.revenue, Double::sum);
            }
            Map<String, double[]> byPart = new LinkedHashMap<>();
            for (SaleRecord r : filtered) {
                byPart.computeIfAbsent(r.partName + "|" + r.sku, k -> new double[2]);
                byPart.get(r.partName + "|" + r.sku)[0] += r.qty;
                byPart.get(r.partName + "|" + r.sku)[1] += r.revenue;
            }
            sections.removeAll();
            sections.add(buildSectionLabel("Revenue Summary (" + start + " → " + end + ")"));
            sections.add(Box.createVerticalStrut(10));
            JPanel statCards = new JPanel(new GridLayout(1, 4, 10, 0));
            statCards.setBackground(BG); statCards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76)); statCards.setAlignmentX(Component.LEFT_ALIGNMENT);
            statCards.add(buildStatCard("Total Revenue",  formatPeso(totalRev),   GREEN_FG));
            statCards.add(buildStatCard("Units Sold",     String.valueOf(totalQty), ORANGE));
            statCards.add(buildStatCard("Daily Avg",      formatPeso(dailyAvg),   BLUE_FG));
            statCards.add(buildStatCard("Months Covered", String.valueOf(byMonth.size()), TEXT));
            sections.add(statCards);
            sections.add(Box.createVerticalStrut(28));
            sections.add(buildSectionLabel("Daily Sales"));
            sections.add(Box.createVerticalStrut(10));
            if (byDay.isEmpty()) {
                sections.add(buildNoDataLabel());
            } else {
                String[] dCols = {"Date", "Revenue"};
                Object[][] dData = new Object[byDay.size()][2];
                int i = 0;
                List<Map.Entry<String,Double>> dayEntries = new ArrayList<>(byDay.entrySet());
                dayEntries.sort(Map.Entry.comparingByKey());
                for (Map.Entry<String,Double> e : dayEntries)
                    dData[i++] = new Object[]{ e.getKey(), formatPeso(e.getValue()) };
                sections.add(buildStyledTable(dCols, dData));
            }
            sections.add(Box.createVerticalStrut(28));
            sections.add(buildSectionLabel("Monthly Sales"));
            sections.add(Box.createVerticalStrut(10));
            if (byMonth.isEmpty()) {
                sections.add(buildNoDataLabel());
            } else {
                String[] mCols = {"Month", "Revenue"};
                Object[][] mData = new Object[byMonth.size()][2];
                int i = 0;
                for (Map.Entry<String,Double> e : byMonth.entrySet())
                    mData[i++] = new Object[]{ e.getKey(), formatPeso(e.getValue()) };
                sections.add(buildStyledTable(mCols, mData));
            }
            sections.add(Box.createVerticalStrut(28));
            sections.add(buildSectionLabel("Annual Sales"));
            sections.add(Box.createVerticalStrut(10));
            if (byYear.isEmpty()) {
                sections.add(buildNoDataLabel());
            } else {
                String[] yCols = {"Year", "Revenue"};
                Object[][] yData = new Object[byYear.size()][2];
                int i = 0;
                for (Map.Entry<String,Double> e : byYear.entrySet())
                    yData[i++] = new Object[]{ e.getKey(), formatPeso(e.getValue()) };
                sections.add(buildStyledTable(yCols, yData));
            }
            sections.add(Box.createVerticalStrut(28));
            sections.add(buildSectionLabel("Top Selling Parts"));
            sections.add(Box.createVerticalStrut(10));
            if (byPart.isEmpty()) {
                sections.add(buildNoDataLabel());
            } else {
                List<Map.Entry<String,double[]>> sorted = new ArrayList<>(byPart.entrySet());
                sorted.sort((a,b) -> Double.compare(b.getValue()[1], a.getValue()[1]));
                String[] pCols = {"Rank","Part Name","SKU","Units Sold","Revenue"};
                Object[][] pData = new Object[sorted.size()][5];
                for (int j = 0; j < sorted.size(); j++) {
                    String[] kp = sorted.get(j).getKey().split("\\|");
                    pData[j] = new Object[]{"#"+(j+1), kp[0], kp.length>1?kp[1]:"—",
                        (int)sorted.get(j).getValue()[0],
                        formatPeso(sorted.get(j).getValue()[1])};
                }
                sections.add(buildStyledTable(pCols, pData));
            }
            sections.add(Box.createVerticalStrut(28));
            sections.add(buildSectionLabel("Monthly Revenue Chart"));
            sections.add(Box.createVerticalStrut(10));
            sections.add(buildFilteredBarChart(byMonth));
            sections.revalidate();
            sections.repaint();
        };
        rebuild[0].run();
        applyBtn.addActionListener(e -> {
            filterError.setText("");
            try {
                LocalDate start = LocalDate.parse(fromField.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                LocalDate end   = LocalDate.parse(toField.getText().trim(),   DateTimeFormatter.ISO_LOCAL_DATE);
                if (start.isAfter(end)) { filterError.setText("Start date must be before end date."); return; }
                rangeRef[0] = start; rangeRef[1] = end;
                rebuild[0].run();
            } catch (DateTimeParseException ex) {
                filterError.setText("Invalid date format. Use YYYY-MM-DD.");
            }
        });
        resetBtn.addActionListener(e -> {
            filterError.setText("");
            fromField.setText("2026-01-01"); fromField.setForeground(TEXT);
            toField.setText("2026-05-22");   toField.setForeground(TEXT);
            rangeRef[0] = LocalDate.parse("2026-01-01");
            rangeRef[1] = LocalDate.parse("2026-05-22");
            rebuild[0].run();
        });
        body.add(sections);
        JScrollPane scroll = new JScrollPane(body);
        scroll.setOpaque(false); scroll.getViewport().setOpaque(false);
        scroll.setBorder(null); scroll.getVerticalScrollBar().setUnitIncrement(14);
        content.add(scroll, BorderLayout.CENTER);
        root.add(content, BorderLayout.CENTER);
        return root;
    }
    private JTextField buildDateField(String initial) {
        JTextField tf = new JTextField(initial, 10);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tf.setForeground(TEXT); tf.setBackground(CARD); tf.setCaretColor(ORANGE);
        tf.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(5,8,5,8)));
        tf.setPreferredSize(new Dimension(110, 30));
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { tf.setBorder(new CompoundBorder(new LineBorder(ORANGE,1), new EmptyBorder(5,8,5,8))); }
            public void focusLost(FocusEvent e)   { tf.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(5,8,5,8))); }
        });
        return tf;
    }
    private JPanel buildFilteredBarChart(Map<String,Double> byMonth) {
        JPanel chart = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD); g2.fillRect(0, 0, getWidth(), getHeight());
                if (byMonth.isEmpty()) {
                    g2.setColor(MUTED); g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                    g2.drawString("No data in selected range.", 20, getHeight()/2);
                    return;
                }
                List<Map.Entry<String,Double>> entries = new ArrayList<>(byMonth.entrySet());
                entries.sort(Map.Entry.comparingByKey());
                int pad = 40, n = entries.size();
                int slotW = (getWidth() - pad*2) / Math.max(n, 1);
                int barW  = Math.max(8, slotW - 12);
                int chartH = getHeight() - 60;
                double maxVal = entries.stream().mapToDouble(Map.Entry::getValue).max().orElse(1);
                for (int i = 0; i < n; i++) {
                    double val = entries.get(i).getValue();
                    int x = pad + i * slotW + (slotW - barW) / 2;
                    int barH = (int)((val / maxVal) * chartH);
                    int y = getHeight() - 30 - barH;
                    g2.setColor(i == n-1 ? ORANGE : new Color(80,55,15));
                    g2.fillRoundRect(x, y, barW, barH, 4, 4);
                    g2.setColor(MUTED); g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    String valLbl = "₱" + (int)(val/1000) + "k";
                    int lw = g2.getFontMetrics().stringWidth(valLbl);
                    g2.drawString(valLbl, x + barW/2 - lw/2, y - 4);
                    g2.setFont(new Font("SansSerif", Font.PLAIN, 9));
                    String monthKey = entries.get(i).getKey();
                    String monthLbl = monthKey.length() >= 7 ? monthKey.substring(5) : monthKey;
                    int mlw = g2.getFontMetrics().stringWidth(monthLbl);
                    g2.drawString(monthLbl, x + barW/2 - mlw/2, getHeight() - 12);
                }
                g2.setColor(BORDER);
                g2.drawLine(pad, getHeight()-30, getWidth()-pad, getHeight()-30);
            }
        };
        chart.setBackground(CARD);
        chart.setBorder(new LineBorder(BORDER,1));
        chart.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
        chart.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        chart.setAlignmentX(Component.LEFT_ALIGNMENT);
        return chart;
    }
    private JLabel buildNoDataLabel() {
        JLabel lbl = new JLabel("No sales data in the selected date range.");
        lbl.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lbl.setForeground(MUTED); lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(new EmptyBorder(0,0,8,0));
        return lbl;
    }
    // ═════════════════════════════════════════════════════════════════════════
    // REUSABLE COMPONENTS
    // ═════════════════════════════════════════════════════════════════════════
    private JScrollPane buildStyledTable(String[] cols, Object[][] data) {
        DefaultTableModel model = new DefaultTableModel(data, cols) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(model);
        table.setBackground(CARD); table.setForeground(TEXT); table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setRowHeight(36); table.setShowGrid(false); table.setIntercellSpacing(new Dimension(0,0));
        table.setSelectionBackground(new Color(50,38,18)); table.setSelectionForeground(ORANGE); table.setFocusable(false);
        JTableHeader header = table.getTableHeader(); header.setBackground(new Color(22,22,22));
        header.setForeground(MUTED); header.setFont(new Font("SansSerif", Font.BOLD, 11));
        header.setBorder(new MatteBorder(0,0,1,0,BORDER)); header.setPreferredSize(new Dimension(0,36));
        ((DefaultTableCellRenderer)header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                lbl.setBorder(new EmptyBorder(0,12,0,12));
                lbl.setBackground(sel ? new Color(50,38,18) : (row%2==0 ? CARD : new Color(30,30,30)));
                lbl.setForeground(TEXT); lbl.setOpaque(true);
                String v = val == null ? "" : val.toString();
                if      (v.equals("Completed")||v.equals("Active"))     { lbl.setForeground(GREEN_FG); }
                else if (v.equals("Pending"))                            { lbl.setForeground(WARN_FG); }
                else if (v.equals("Cancelled")||v.equals("Inactive"))   { lbl.setForeground(RED_FG); }
                else if (v.startsWith("#"))                              { lbl.setForeground(ORANGE); }
                return lbl;
            }
        });
        JScrollPane sp = new JScrollPane(table); sp.setOpaque(false); sp.getViewport().setBackground(CARD);
        sp.setBorder(new LineBorder(BORDER,1));
        int rowH = 36, headerH = 36;
        int tableH = Math.min(240, headerH + data.length * rowH + 4);
        sp.setPreferredSize(new Dimension(Integer.MAX_VALUE, tableH));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, tableH));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sp;
    }
    private JPanel buildSidebar(String active) {
        JPanel sidebar = new JPanel(); sidebar.setBackground(SIDEBAR); sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200,0)); sidebar.setBorder(new MatteBorder(0,0,0,1,BORDER));
        JPanel logo = new JPanel(); logo.setBackground(SIDEBAR); logo.setLayout(new BoxLayout(logo, BoxLayout.Y_AXIS));
        logo.setBorder(new EmptyBorder(20,16,20,16)); logo.setMaximumSize(new Dimension(Integer.MAX_VALUE,75));
        JLabel logoTitle = new JLabel("⚙ MotorShop"); logoTitle.setFont(new Font("SansSerif", Font.BOLD, 16)); logoTitle.setForeground(ORANGE);
        JLabel logoSub = new JLabel("Parts Ordering System"); logoSub.setFont(new Font("SansSerif", Font.PLAIN, 10)); logoSub.setForeground(MUTED);
        logo.add(logoTitle); logo.add(Box.createVerticalStrut(3)); logo.add(logoSub); sidebar.add(logo); sidebar.add(makeDivider());
        String[][] items = {{"dashboard","  Dashboard"},{"parts","  Parts"},{"orders","  Orders"},{"customers","  Customers"},{"reports","  Reports"}};
        for (String[] item : items) {
            JPanel nav = makeSidebarItem(item[1], item[0].equals(active)); String key = item[0];
            nav.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { cardLayout.show(mainPanel, key); } });
            sidebar.add(nav);
        }
        sidebar.add(Box.createVerticalGlue()); sidebar.add(makeDivider());
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12)); footer.setBackground(SIDEBAR); footer.setMaximumSize(new Dimension(Integer.MAX_VALUE,55));
        JLabel userIcon = new JLabel("👤"); userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        JPanel userInfo = new JPanel(); userInfo.setBackground(SIDEBAR); userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        JLabel userName = new JLabel(loggedInUser); userName.setFont(new Font("SansSerif", Font.BOLD, 12)); userName.setForeground(new Color(200,200,200));
        JLabel userRole = new JLabel("Staff"); userRole.setFont(new Font("SansSerif", Font.PLAIN, 10)); userRole.setForeground(MUTED);
        userInfo.add(userName); userInfo.add(userRole); footer.add(userIcon); footer.add(userInfo); sidebar.add(footer);
        return sidebar;
    }
    private JPanel makeSidebarItem(String label, boolean active) {
        JPanel p = new JPanel(new BorderLayout()); p.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));
        p.setBackground(active ? new Color(30,26,20) : SIDEBAR);
        p.setBorder(new MatteBorder(0, active?3:0, 0, 0, active?ORANGE:SIDEBAR));
        JLabel lbl = new JLabel(label); lbl.setFont(new Font("SansSerif", Font.PLAIN, 13)); lbl.setForeground(active?ORANGE:MUTED);
        lbl.setBorder(new EmptyBorder(0, active?13:16, 0, 16)); p.add(lbl, BorderLayout.CENTER);
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (!active) { p.setBackground(new Color(26,26,26)); lbl.setForeground(TEXT); } }
            public void mouseExited(MouseEvent e)  { if (!active) { p.setBackground(SIDEBAR); lbl.setForeground(MUTED); } }
        });
        return p;
    }
    private JSeparator makeDivider() {
        JSeparator s = new JSeparator(); s.setForeground(BORDER); s.setBackground(BORDER); s.setMaximumSize(new Dimension(Integer.MAX_VALUE,1)); return s;
    }
    private JPanel buildTopBar(String title) {
        JPanel bar = new JPanel(new BorderLayout()); bar.setBackground(TOPBAR); bar.setPreferredSize(new Dimension(0,54));
        bar.setBorder(new CompoundBorder(new MatteBorder(0,0,1,0,BORDER), new EmptyBorder(0,20,0,20)));
        JLabel lbl = new JLabel(title); lbl.setFont(new Font("SansSerif", Font.BOLD, 15)); lbl.setForeground(TEXT); bar.add(lbl, BorderLayout.WEST);
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0)); right.setBackground(TOPBAR);
        JPanel cartWrapper = new JPanel(null); cartWrapper.setBackground(TOPBAR); cartWrapper.setPreferredSize(new Dimension(60,38));
        JButton cartBtn = makeDarkButton("🛒 Cart"); cartBtn.setFont(new Font("SansSerif", Font.BOLD, 12)); cartBtn.setBounds(0,5,60,28);
        cartBtn.addActionListener(e -> showCartDialog()); cartWrapper.add(cartBtn);
        cartBadge = new JLabel("0"); cartBadge.setFont(new Font("SansSerif", Font.BOLD, 9)); cartBadge.setForeground(new Color(30,15,0));
        cartBadge.setBackground(ORANGE); cartBadge.setOpaque(true); cartBadge.setHorizontalAlignment(SwingConstants.CENTER);
        cartBadge.setBounds(42,2,16,16); cartBadge.setBorder(BorderFactory.createLineBorder(TOPBAR,1)); cartBadge.setVisible(false);
        cartWrapper.add(cartBadge); right.add(cartWrapper);
        JButton logoutBtn = makeDarkButton("Logout"); logoutBtn.addActionListener(e -> doLogout()); right.add(logoutBtn);
        bar.add(right, BorderLayout.EAST); return bar;
    }
    private JPanel buildStatsRow() {
        JPanel row = new JPanel(new GridLayout(1,4,10,0)); row.setBackground(BG); row.setMaximumSize(new Dimension(Integer.MAX_VALUE,76)); row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(buildStatCard("Total Parts","248",TEXT)); row.add(buildStatCard("Low Stock","12",WARN_FG)); row.add(buildStatCard("Orders Today","7",ORANGE)); row.add(buildStatCard("Revenue","₱18,400",GREEN_FG));
        return row;
    }
    private JPanel buildStatCard(String label, String value, Color valColor) {
        JPanel p = new JPanel(); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(12,16,12,16)));
        JLabel lbl = new JLabel(label); lbl.setFont(new Font("SansSerif", Font.PLAIN, 11)); lbl.setForeground(MUTED);
        JLabel val = new JLabel(value); val.setFont(new Font("SansSerif", Font.BOLD, 22)); val.setForeground(valColor);
        p.add(lbl); p.add(Box.createVerticalStrut(5)); p.add(val); return p;
    }
    private JPanel buildPartsGrid() {
        JPanel grid = new JPanel(new GridLayout(0,3,10,10)); grid.setBackground(BG); grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        Object[][] parts = {
            {"Engine Oil Filter","SKU: ENG-001","₱320",320.0,true},
            {"Spark Plug Set",   "SKU: IGN-042","₱180",180.0,true},
            {"Brake Disc Pad",   "SKU: BRK-017","₱650",650.0,false},
            {"Coolant Fluid 1L", "SKU: CLT-008","₱220",220.0,true},
            {"Drive Belt",       "SKU: DRV-033","₱480",480.0,false},
            {"Alternator",       "SKU: ALT-005","₱2,800",2800.0,true},
            {"Air Filter",       "SKU: AIR-011","₱260",260.0,true},
            {"Timing Chain Kit", "SKU: TIM-022","₱1,450",1450.0,true},
            {"Fuel Injector",    "SKU: FUL-039","₱3,100",3100.0,false}
        };
        for (Object[] p : parts)
            grid.add(buildPartCard((String)p[0],(String)p[1],(String)p[2],(Double)p[3],(Boolean)p[4]));
        return grid;
    }

    // ── Updated part card: adds image slot at top ──────────────────────────
    private JPanel buildPartCard(String name, String sku, String price, double unitPrice, boolean inStock) {
        JPanel p = new JPanel(new BorderLayout(0, 0));
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(12,12,12,12)));

        // ── Image slot (top) ───────────────────────────────────────────────
        String cleanSku = sku.replace("SKU: ","");
        JPanel imgSlot = buildImageSlot(cleanSku);
        imgSlot.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(imgSlot, BorderLayout.NORTH);

        // ── Info section (centre) ──────────────────────────────────────────
        JPanel info = new JPanel();
        info.setBackground(CARD);
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBorder(new EmptyBorder(10, 2, 0, 2));

        JLabel nameLbl = new JLabel(name);
        nameLbl.setFont(new Font("SansSerif", Font.BOLD, 13));
        nameLbl.setForeground(TEXT);

        JLabel skuLbl = new JLabel(sku);
        skuLbl.setFont(new Font("SansSerif", Font.PLAIN, 11));
        skuLbl.setForeground(MUTED);

        info.add(nameLbl);
        info.add(Box.createVerticalStrut(3));
        info.add(skuLbl);
        p.add(info, BorderLayout.CENTER);

        // ── Price / stock / button (bottom) ───────────────────────────────
        JPanel bottom = new JPanel();
        bottom.setBackground(CARD);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new EmptyBorder(8, 2, 0, 2));

        JPanel midRow = new JPanel(new BorderLayout());
        midRow.setBackground(CARD);

        JLabel priceLbl = new JLabel(price);
        priceLbl.setFont(new Font("SansSerif", Font.BOLD, 14));
        priceLbl.setForeground(ORANGE);

        JLabel stockLbl = new JLabel(inStock ? "In stock" : "Low stock");
        stockLbl.setFont(new Font("SansSerif", Font.PLAIN, 10));
        stockLbl.setForeground(inStock ? GREEN_FG : WARN_FG);
        stockLbl.setOpaque(true);
        stockLbl.setBackground(inStock ? GREEN_BG : WARN_BG);
        stockLbl.setBorder(new EmptyBorder(3,8,3,8));

        midRow.add(priceLbl, BorderLayout.WEST);
        midRow.add(stockLbl, BorderLayout.EAST);

        JButton addBtn = makeOrangeButton("+ Add to Cart");
        addBtn.setFont(new Font("SansSerif", Font.BOLD, 11));
        addBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        addBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addBtn.addActionListener(e -> addToCart(name, cleanSku, price, unitPrice));

        bottom.add(midRow);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(addBtn);
        p.add(bottom, BorderLayout.SOUTH);

        // ── Hover highlight ────────────────────────────────────────────────
        p.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        p.addMouseListener(new MouseAdapter() {
            final Color hover = new Color(40, 40, 40);
            public void mouseEntered(MouseEvent e) {
                p.setBackground(hover); info.setBackground(hover);
                bottom.setBackground(hover); midRow.setBackground(hover);
                imgSlot.setBackground(hover);
            }
            public void mouseExited(MouseEvent e) {
                p.setBackground(CARD); info.setBackground(CARD);
                bottom.setBackground(CARD); midRow.setBackground(CARD);
                imgSlot.setBackground(CARD);
            }
        });
        return p;
    }

    private JLabel buildSectionLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase()); lbl.setFont(new Font("SansSerif", Font.BOLD, 10)); lbl.setForeground(MUTED); lbl.setAlignmentX(Component.LEFT_ALIGNMENT); return lbl;
    }
    private JButton makeNavCard(String title, String sub) {
        JButton btn = new JButton("<html><b>"+title+"</b><br><span style='color:#888;font-size:10px'>"+sub+"</span></html>");
        btn.setFont(new Font("SansSerif", Font.PLAIN, 13)); btn.setForeground(TEXT); btn.setBackground(CARD);
        btn.setPreferredSize(new Dimension(170,64)); btn.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(8,14,8,14)));
        btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(42,32,15)); btn.setBorder(new CompoundBorder(new LineBorder(ORANGE,1), new EmptyBorder(8,14,8,14))); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(CARD); btn.setBorder(new CompoundBorder(new LineBorder(BORDER,1), new EmptyBorder(8,14,8,14))); }
        });
        return btn;
    }
    private JButton makeOrangeButton(String text) {
        JButton btn = new JButton(text); btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(new Color(30,15,0)); btn.setBackground(ORANGE); btn.setOpaque(true);
        btn.setBorderPainted(false); btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6,16,6,16));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(255,165,40)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(ORANGE); }
        });
        return btn;
    }
    private JButton makeDarkButton(String text) {
        JButton btn = new JButton(text); btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(new Color(200,200,200)); btn.setBackground(new Color(34,34,34)); btn.setOpaque(true);
        btn.setBorder(new LineBorder(BORDER,1)); btn.setFocusPainted(false); btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50,50,50)); btn.setBorder(new LineBorder(ORANGE,1)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(34,34,34)); btn.setBorder(new LineBorder(BORDER,1)); }
        });
        return btn;
    }
}