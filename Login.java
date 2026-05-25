package lance;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;

public class Login {

    JFrame frame;
    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JTextField     txtSecurity;

    // Detalye ng iyong MySQL Database Server
    private final String DB_URL = "jdbc:mysql://localhost:3307/databasetest?useSSL=false&serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASS = "Lance2007!";

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try { new Login().frame.setVisible(true); }
            catch (Exception e) { e.printStackTrace(); }
        });
    }

    public Login() { initialize(); }

    private void initialize() {
        frame = new JFrame("MotorShop - Login");
        frame.setSize(500, 520);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(17, 17, 17));
        frame.setContentPane(panel);

        JPanel stripe = new JPanel();
        stripe.setBackground(new Color(255, 140, 0));
        stripe.setBounds(0, 0, 500, 4);
        panel.add(stripe);

        // LOGO IMAGE (embedded base64 PNG, transparent background)
        try {
            String logoBase64 = "iVBORw0KGgoAAAANSUhEUgAAAFoAAAA3CAYAAACMwl2GAAAJJmlDQ1BpY2MAAEiJlZVnUJNZF8fv8zzp" +
            "hUASQodQQ5EqJYCUEFoo0quoQOidUEVsiLgCK4qINEWQRQEXXJUia0UUC4uCAhZ0gywCyrpxFVFBWXDf" +
            "GZ33HT+8/5l7z2/+c+bec8/5cAEgiINlwct7YlK6wNvJjhkYFMwE3yiMn5bC8fR0A9/VuxEArcR7ut/P" +
            "+a4IEZFp/OW4uLxy+SmCdACg7GXWzEpPWeGjy0wPj//CZ1dYsFzgMt9Y4eh/eexLzr8s+pLj681dfhUK" +
            "ABwp+hsO/4b/c++KVDiC9NioyGymT3JUelaYIJKZttIJHpfL9BQkR8UmRH5T8P+V/B2lR2anr0Rucsom" +
            "QWx0TDrzfw41MjA0BF9n8cbrS48hRv9/z2dFX73kegDYcwAg+7564ZUAdO4CQPrRV09tua+UfAA67vAz" +
            "BJn/eqiVDQ0IgALoQAYoAlWgCXSBETADlsAWOAAX4AF8QRDYAPggBiQCAcgCuWAHKABFYB84CKpALWgA" +
            "TaAVnAad4Dy4Aq6D2+AuGAaPgRBMgpdABN6BBQiCsBAZokEykBKkDulARhAbsoYcIDfIGwqCQqFoKAnK" +
            "gHKhnVARVApVQXVQE/QLdA66At2EBqGH0Dg0A/0NfYQRmATTYQVYA9aH2TAHdoV94fVwNJwK58D58F64" +
            "Aq6HT8Id8BX4NjwMC+GX8BwCECLCQJQRXYSNcBEPJBiJQgTIVqQQKUfqkVakG+lD7iFCZBb5gMKgaCgm" +
            "ShdliXJG+aH4qFTUVlQxqgp1AtWB6kXdQ42jRKjPaDJaHq2DtkDz0IHoaHQWugBdjm5Et6OvoYfRk+h3" +
            "GAyGgWFhzDDOmCBMHGYzphhzGNOGuYwZxExg5rBYrAxWB2uF9cCGYdOxBdhK7EnsJewQdhL7HkfEKeGM" +
            "cI64YFwSLg9XjmvGXcQN4aZwC3hxvDreAu+Bj8BvwpfgG/Dd+Dv4SfwCQYLAIlgRfAlxhB2ECkIr4Rph" +
            "jPCGSCSqEM2JXsRY4nZiBfEU8QZxnPiBRCVpk7ikEFIGaS/pOOky6SHpDZlM1iDbkoPJ6eS95CbyVfJT" +
            "8nsxmpieGE8sQmybWLVYh9iQ2CsKnqJO4VA2UHIo5ZQzlDuUWXG8uIY4VzxMfKt4tfg58VHxOQmahKGE" +
            "h0SiRLFEs8RNiWkqlqpBdaBGUPOpx6hXqRM0hKZK49L4tJ20Bto12iQdQ2fRefQ4ehH9Z/oAXSRJlTSW" +
            "9JfMlqyWvCApZCAMDQaPkcAoYZxmjDA+SilIcaQipfZItUoNSc1Ly0nbSkdKF0q3SQ9Lf5RhyjjIxMvs" +
            "l+mUeSKLktWW9ZLNkj0ie012Vo4uZynHlyuUOy33SB6W15b3lt8sf0y+X35OQVHBSSFFoVLhqsKsIkPR" +
            "VjFOsUzxouKMEk3JWilWqUzpktILpiSTw0xgVjB7mSJleWVn5QzlOuUB5QUVloqfSp5Km8oTVYIqWzVK" +
            "tUy1R1WkpqTmrpar1qL2SB2vzlaPUT+k3qc+r8HSCNDYrdGpMc2SZvFYOawW1pgmWdNGM1WzXvO+FkaL" +
            "rRWvdVjrrjasbaIdo12tfUcH1jHVidU5rDO4Cr3KfFXSqvpVo7okXY5upm6L7rgeQ89NL0+vU++Vvpp+" +
            "sP5+/T79zwYmBgkGDQaPDamGLoZ5ht2GfxtpG/GNqo3uryavdly9bXXX6tfGOsaRxkeMH5jQTNxNdpv0" +
            "mHwyNTMVmLaazpipmYWa1ZiNsulsT3Yx+4Y52tzOfJv5efMPFqYW6RanLf6y1LWMt2y2nF7DWhO5pmHN" +
            "hJWKVZhVnZXQmmkdan3UWmijbBNmU2/zzFbVNsK20XaKo8WJ45zkvLIzsBPYtdvNcy24W7iX7RF7J/tC" +
            "+wEHqoOfQ5XDU0cVx2jHFkeRk4nTZqfLzmhnV+f9zqM8BR6f18QTuZi5bHHpdSW5+rhWuT5z03YTuHW7" +
            "w+4u7gfcx9aqr01a2+kBPHgeBzyeeLI8Uz1/9cJ4eXpVez33NvTO9e7zofls9Gn2eedr51vi+9hP0y/D" +
            "r8ef4h/i3+Q/H2AfUBogDNQP3BJ4O0g2KDaoKxgb7B/cGDy3zmHdwXWTISYhBSEj61nrs9ff3CC7IWHD" +
            "hY2UjWEbz4SiQwNCm0MXwzzC6sPmwnnhNeEiPpd/iP8ywjaiLGIm0iqyNHIqyiqqNGo62ir6QPRMjE1M" +
            "ecxsLDe2KvZ1nHNcbdx8vEf88filhICEtkRcYmjiuSRqUnxSb7JicnbyYIpOSkGKMNUi9WCqSOAqaEyD" +
            "0tandaXTlz/F/gzNjF0Z45nWmdWZ77P8s85kS2QnZfdv0t60Z9NUjmPOT5tRm/mbe3KVc3fkjm/hbKnb" +
            "Cm0N39qzTXVb/rbJ7U7bT+wg7Ijf8VueQV5p3tudATu78xXyt+dP7HLa1VIgViAoGN1tubv2B9QPsT8M" +
            "7Fm9p3LP58KIwltFBkXlRYvF/OJbPxr+WPHj0t6ovQMlpiVH9mH2Je0b2W+z/0SpRGlO6cQB9wMdZcyy" +
            "wrK3BzcevFluXF57iHAo45Cwwq2iq1Ktcl/lYlVM1XC1XXVbjXzNnpr5wxGHh47YHmmtVagtqv14NPbo" +
            "gzqnuo56jfryY5hjmceeN/g39P3E/qmpUbaxqPHT8aTjwhPeJ3qbzJqamuWbS1rgloyWmZMhJ+/+bP9z" +
            "V6tua10bo63oFDiVcerFL6G/jJx2Pd1zhn2m9az62Zp2WnthB9SxqUPUGdMp7ArqGjzncq6n27K7/Ve9" +
            "X4+fVz5ffUHyQslFwsX8i0uXci7NXU65PHsl+spEz8aex1cDr97v9eoduOZ67cZ1x+tX+zh9l25Y3Th/" +
            "0+LmuVvsW523TW939Jv0t/9m8lv7gOlAxx2zO113ze92D64ZvDhkM3Tlnv296/d5928Prx0eHPEbeTAa" +
            "Mip8EPFg+mHCw9ePMh8tPN4+hh4rfCL+pPyp/NP637V+bxOaCi+M24/3P/N59niCP/Hyj7Q/Fifzn5Of" +
            "l08pTTVNG02fn3Gcufti3YvJlykvF2YL/pT4s+aV5quzf9n+1S8KFE2+Frxe+rv4jcyb42+N3/bMec49" +
            "fZf4bmG+8L3M+xMf2B/6PgZ8nFrIWsQuVnzS+tT92fXz2FLi0tI/QiyQvpNzTVQAAAAgY0hSTQAAeiYA" +
            "AICEAAD6AAAAgOgAAHUwAADqYAAAOpgAABdwnLpRPAAAAAZiS0dEAP8A/wD/oL2nkwAAAAd0SU1FB+oF" +
            "GA0TGFwdKNMAAAE9elRYdFJhdyBwcm9maWxlIHR5cGUgeG1wAAAokXVSS27EIAzdc4oegfhhmxwnE2BX" +
            "qcsev88wo8xHBQkSG7+PIf1+/6SvGJCccGJ4tWanZDdTF4ftNqyjiWCTjiwi2dS6gfHCbDePrKqWYtJQ" +
            "RRIGej5wesURgJ4Jql5sI/AksO6IXJTaRqBhYopjxR+nk4zgu2AYvE22WppmyYUqnIWSUaiuLIXopBaC" +
            "Gg6VQl3pjXclg7t64cw4SDV8DunOQ9IntMvAhj0mv9gj4SZc2wLhDmcXQq5XaUEQ6WcNFKDsJPi/Tw/s" +
            "abJOTfcD7ICQjZ5D0ypmJjoRRUroEmpejIGgG3q6QrS3oytvjmWzQOfOu7rJurFG9aAhR0WGzvWUMFrS" +
            "BfEIPfNE7D9N1+mwmt698v28Qs47ucc+nggt/QHsLpwXI6q7cwAAL0FJREFUeNrdvHl4VFW67//de1fV" +
            "rnlKVVKpzHPICARIGELCLKDQQKtHQWwVQbtbbZv29PF2t6207fV0o8dZWlt+IihOoC3IICRAIAkJQ+ax" +
            "EpKQOZXUlJr2fP9ok4u259zrbfvYz+99nvVkP6lVtdf67Hevd73D3kR1dTXNMEwcSZKDTqcztHHjRhAE" +
            "gRulra0NZWVlSEtLe0gURaSnp7/tdDonCwsL/6bv/19FkiRUV1cjMjJS53A4fkSSJDo7O19eunQpsrOz" +
            "/6bvJ598AqvVquJ5Poam6X5Sr9fzLMuuHx4ePsDz/I+ampoSJUmigsHg9BctFgtiY2PBcdxQRUXF0wcP" +
            "HtwfCASWsCyruHbtGiRJ+odM7le/+hVOnjxJrlmzRpWRkWFJT09PpGk6mabp5LS0tMT09HTL2rVrVSdP" +
            "niR//etf/8Mg9/T0IBwOy4PBYMnBgwffqaio+D3P88NxcXGIjIyc7hcMBiFJEtXY2JjIcdyPBgcHD7As" +
            "u16n0/FEWVkZ9Hp93Pnz5w+dPHlybmxs7LWcnJyzqampRzIyMi6mpaWNiaIonj59GpGRkfEvv/zy6ePH" +
            "j6fNnz/fvXjx4uP5+fl/njlzZvXAwEA4JyfnbzRckiRcuHABJEmaaZoOEATBFBQUfOOE9u7di3vuuYco" +
            "LCzU9/f3Z/A8P4+iqFlyuTxRoVCYlUqllqIoEgBEURQZhgkwDDPBcVwvz/N1SqXyclxcXFtlZaVv7969" +
            "0n333feN56mrqwMAmmEYnUKhGJ89e/bf9JEkCU1NTYiLi1NevXq1qK6ubmtlZeUPLl68aFq7dm3PQw89" +
            "tHx8fPza4sWLIZPJiM7OziiHw7HA4XCsbWpqKh0cHExesWJF9aJFi271eDyDRHd3N8rKyhATE7Pk4MGD" +
            "Bw4dOmQ3GAzIzMxkc3NzHZmZmV+kpqaeyMjIuJSQkODZs2fPc7t37360u7sbdrsdc+bMmZw3b15ZTk7O" +
            "wezs7HOpqaljoihKFEUBAKqqqjB//nzyo48++qMoir5FixbtHhsbC9w4uaVLl8Jms8kuXLiQybLsRpqm" +
            "V1kslriEhAQpJSUlEBMTw+j1elahUMgkSZJxHIdwOCwFg0HR6/WS4+Pj9NDQkGZoaIgcGxvrC4fDJ2ma" +
            "Prx48eL24eFhvqysbPpcV65cQVRUlLqysvIXBEHobr311l9WV1eLCxYswJcXEARBEF1dXZGtra1Lmpqa" +
            "/qW2tnbp5cuXdUNDQ0hJScFjjz32wo4dO3b29fUZOzo65jgcjlVtbW0rm5ub09vb2xU+nw8//OEPR+68" +
            "884tTqezLDs7G0RlZaWeIIjwggUL2I8//njLu++++9LRo0dNHMdBLpfDZrNhxowZoezs7KaMjIyTM2bM" +
            "GDl06NBv9+zZE8myLFQqlUur1RozMzOF2bNnt2dnZx9LSUk5MmPGjMbo6Gh/dXW1lJ6ebnr22WdP1dfX" +
            "z9ywYcObixcv3tXY2Dh86NAhZGRkkO+++25+MBjcHhkZeVNOTo5UXFw8mpWVJanVahPHceZwOKwNhUKK" +
            "YDBIBYNBBINBhMNhhEIhkWEYluO4gCAIXrVa7dPr9dzly5cjm5qaMDY2dlyv1//5Zz/7WUN7e7uwaNEi" +
            "5ObmRp87d+43n3766faZM2fWP/744ys6OzvdRUVFxNDQkK69vT23q6trbWtr69qrV69mtbe3k36/3xMK" +
            "hcwKhQI7duwY3bRp05Pt7e32jo6Om1paWnLa2tpUIyMjmGJ28803u7ds2fLIpk2b9l+6dIkOBoNy4tSp" +
            "U/8yMjKygSTJ8oyMjJrBwcGSAwcO/Pbo0aMmlUrVHggERJZl0xUKhcxutyMrK8ufl5cnO3v2rPLixYuI" +
            "j4//kyiKDqfT+YAkSakxMTGYMWOGLysrqyEtLe1MYmLi2fz8/P7nn3/+4IsvvjgnOTlZ2rhxY+WSJUue" +
            "2L17d8elS5e2WyyWHy1YsCC0cePGkYSEBCvP83GhUEjHsizBsixYlkU4HEYwGEQoFPrK3ynoer0eeXl5" +
            "mDFjxqQgCAM9PT2eEydORFZXV8vGx8f3FRQU/Gnnzp0ZZ8+e3XX48OGF165dIx555JHLjz766J1NTU2x" +
            "vb29SxwOx9LW1tbc1tZW/dDQECRJckRGRv6JIIi0/v7+HUVFRViyZEm4oaGBb21t1Q4NDYFlWZ6maYda" +
            "rSZDoVDGl5B3xcbGnmlvby8CsMxqtR4iampqsj/44IOjZ8+eTUxJSRlPSUnpTElJia2trY0/c+ZMe0RE" +
            "xM9HR0cNHo9nQyAQKOY4zkbTNKHVauFyuaDRaC4tWLBgLcuyhtbW1ns9Hs9WlmVjlEolYmJikJqaGkxP" +
            "T+82m81Rr7/+euTY2BjMZjMKCwvHe3t7x5KTk7Xbtm3rysvLi2IYJoVhGOUU3KnGMMxXAN8IWRRF5Ofn" +
            "IysrCzKZDKFQCOFwGCzLcjzP9w4MDEwcOXIk0eFwTCQmJkbV1NRYXC4XIiMj8eCDD4653W5nZ2dnUldX" +
            "l3pwcBChUAgKhWLQYDDsS0lJectgMExWVlZ+HggE5prNZvj9fjAMI8nl8hGNRnPeaDR+YrFY3B6P54Ul" +
            "S5ZkFhYW9nd1dQ10d3enXbt2zVJSUtJ7++23ryUkSSJfeeWVf3/ppZd+4XA4oFQqYTQaER0dDa/XC5/P" +
            "V5aamnr3smXLRv/yl7+kud3uVZOTk7cEg8ECnucNFEUJdrv9sf7+/v9oa2sz7ty5M6O2tvZun893G8uy" +
            "EQAgl8uRk5ODxMREiKKIqqoqCIIgbd++/eq9997rVygUs8LhsP7rgDmOA8uyX4F7I2SGYTB37lzk5+dP" +
            "95u6KFP9GIYJkSTZeeXKFcWBAwcySZIkFixYAJIk0dvbi+bmZnAcBwBQKBQTarX6/fnz5+/fvXt3e3Z2" +
            "tjcuLu7RoaGhPwqCQMlkMq9Go6kzGAxHdDrd8R/84AeO8vLyqM7Ozrf1ev1yg8GAkZEReDwehMNhpKWl" +
            "4eGHH97905/+9JdUcXGxlJKS0g1gUUdHh93n88Hv92NkZARutxuhUCjZ6/WmhEKh6pGREUdERMTF7Ozs" +
            "jwGUkSQ5KoqigWGYpVFRUWO33HKLa926dbdu3rz5VH19/fuhUMggimL60qVLicLCQnAcB6vVirVr1yIY" +
            "DHKpqalibm5uDgAVx3HgeR48z0MQhOnjKdg3avfURUhPT0dBQQEEQQDDMNPty/V7qsmdTmdkV1eXaDAY" +
            "lFu3bqUkSYIoisjNzYVWq8XAwICo0+kO5+Tk/OLQoUOda9euXR0MBruPHTt2k9vt/hVJkn1Go3FvdHT0" +
            "k/n5+c+zLFvhdrvHPR5PQnNz80ter3eN2+3GyMgI/H4/eJ6HxWLB5s2br6xYseKXbW1tLmrXrl344osv" +
            "3PPnz29Uq9VFvb29kX6//ytbHY7jMl0u1wIACSzLZun1evb06dN1O3bs6ExOTi7v6+u76vV6Z37xxRea" +
            "efPm+TUaze/WrFljvnz5crCoqGhmTk4OMTw8jEAgAIqiUFRUBKvVSrW0tBhra2vJuLg4aLVa3Aib53mI" +
            "ogie5xEOh+H3++HxeODxeOD1emGz2bBgwQLIZDJwHPeNkBmGweDgIM6dO0fodDptaWkplZycjNbW1mkg" +
            "WVlZsNlskkqlqn3llVdyJEl6cHh4+NT27dttoVAoNyEhYf9TTz114Pnnnz+7Y8eOjgMHDuQNDAxsZBhm" +
            "3eDg4L/5/f6lX/cjoqOjsWXLluabbrrpwcuXLzcVFxeDAIDR0VG8/vrrWLZs2cwzZ8788ZNPPlna2NhI" +
            "0jQ9TpJkH8dxUYIgRIiiqFKpVP7Y2Ng/Go3Gt9566y2WpulFPM/nh0KhQH19/ajP5+tevnz5jMOHD//B" +
            "4XBoc3Nz5YODgwiHwwgEAkhMTMTcuXNx/vx55Ofnw+FwoL6+HqtXr0ZBQQE4jpsG19/fj+bmZvT29sLr" +
            "9UIQhKktmKBWqwm9Xk8ajUYkJiYiNTUVer1++jyBQAANDQ1oaWlBaWkp0tPTUV9fj+LiYly6dAm9vb3Q" +
            "aDRQqVSIiYlBY2Mjm5aWFtiwYcPPT5w40WYymdJmz54do1KpdDKZrI5l2Yp77rlH4fV67xscHHwsGAxq" +
            "SZIMURQ1IZfLR0VRTGAYxpKbmytu2LChfOnSpY+dOnWq/ic/+QmioqJAXL58WR4fH8+TJCl1dHTAZrNF" +
            "VFZWPnz69Okf19TUKAD8wWKxHO7v7zfxPJ8iimIaQRCxFEWF5HJ5s8ViaXzggQeQn59/C0VRN5EkKZw8" +
            "ebJl7969s++55x4ty7IJgUAA4XAYDMNg0aJFoGkaNTU1KCwshNlsRnd3N44fP47Y2FisXLkSIyMjKC8v" +
            "h9PphNVqhUajQWRkJEiSRF1dHUKhUOf69ev/3el0xo+MjGxlGCZ2bGxMrtfrMXv2bCiVSlRUVIDjOKxf" +
            "vx7Jyclwu92oqanBvHnzEA6HceHCBSiVSiiVSqjVatA0fX3fvn2Be++9t2H58uWpoiiSgiCcaGhoOPbG" +
            "G2/A6XTm8Tyfw/O8SpKkAZIkHTKZrDsmJsbldDo3UBT1b4WFhezy5ctfW7BgwUsjIyMTX7rmxLVr12RE" +
            "eXn5Ay6XK29oaIhVq9VgGEYMh8NaURSXtra2ppw4cSLE8/zb6enpzxoMhuuHDx9W9vb25g0MDGQODQ2Z" +
            "3G43FwwGr8fFxfXMmTMHvb292//4xz/+2GazkUVFRQiFQuSUYTKZTCgtLUVDQwNcLhcKCwtBkiREUYTP" +
            "58Pp06fR2NiIcDiMJUuWoLCwEK2trXjhhRdQUFCApKQkHDp0CF6vdzQ3N3et0+mc6fP5XnriiScUixcv" +
            "ln322Wc4evQoKIrCvHnzsHLlSuh0uulz1NTUwGw2Iz8/H2fOnIHH44FarYZKpYJKpRIvXryIkZER8bHH" +
            "Hns9ISFhz+XLlzEwMJCsVCrjIiIi5DabzRUfH98ZHx/fuGnTpvDk5GR8Z2fnv5Ekeffq1avV2dnZ3QRB" +
            "lCuVSr9CoSBDoRBiYmKUZrO5iTh//nzcwMDAE0eOHPlRVVWVbMrYhEIhsCw7fbuq1epmvV7/amJi4l8q" +
            "KirGg8FghNPpzA+Hw4t4ns8VRdFEkuTgb3/7W43P57v5zjvvJAmCmN7nMgzDFBUVuUwmk+3cuXNEdHQ0" +
            "UlJSwHEcBEGYbnV1dTh58iRCoRB++MMf4tq1a9izZw8UCgVUKhXcbjckSRJpmh4EoAyHw9bHHnsMK1as" +
            "wPPPPw+WZbF69WrMnDkTFEWBoijIZDLIZDJ0d3djeHgYJSUlktvtHrl48aKZpml6SqtFUcTBgwdFg8Fw" +
            "7Mknn/SKohhDkqRPJpM10jR9PjIysl6lUrlLSkosvb29630+30+CwWAOAFAUNT1GhUIBmqYxf/58/pZb" +
            "btkbFxf3NFFZWYmIiAhNTU3Ng8eOHfvX8+fPW6e2Ox6PZ3rrAwAEQQgqlcqh1WrLKIqqANAZFRU18dBD" +
            "D6lnzpw5o62t7Y49e/bcumHDBiIpKekr+96RkZFrGRkZv2FZ9j5BEIrnzZsnl8vl0zuIKUMoSRJGR0fx" +
            "wQcf4OrVq5iYmEAwGOSUSuWkVqsNK5VKVhRFkWVZZSAQ0IRCIY1KpZLFxsYiJycHt95661/XRIIARVGQ" +
            "y+VQKBRQKBTgOA61tbUCSZJnaJp+u6Oj43c2my3pS42GWq1Gb28vDh06JD3wwAMfZmVlfVhfX9/yyiuv" +
            "BEdGRiIApAuCsNjv9y8Lh8NpoihSU2zkcjkMBgMIgoBcLkdxcbFzzZo1fygsLHzN5XIFCQDo6+tDfHw8" +
            "dfz48ZKhoaEnxsfHFx0+fJiqq6sDx3FQKpUjJEl6qL8GMIhgMDgpk8nCarV6kqbpMZ1ONxwZGXl9YGCg" +
            "uKSk5LYtW7aA5/lp52FoaAhnzpyRRkdH25VK5ZUf//jHoxkZGZsrKipsPp8Pc+bMgcVimd7aiaIIhmFw" +
            "9uxZvPfee2JcXFx3dna2RNO0jqIoWpIkURAElmXZYHt7O9nb25t4xx13kKWlpaBp+iua7HQ6ceXKFej1" +
            "eixevHi4vb39vT179tgYhpkTGRmZXlpaStjtdiiVSqhUKshkMhw4cADnzp37IDY29oLL5Ur0eDy2cDhs" +
            "DQaDOp7nlWq1WgdAEgRBEEXRGA6HbXK5HLNmzcLGjRsFq9V6ITo6etfq1avPXb9+XUhISIAMABISEsDz" +
            "vDAxMVE+d+7chvb29tvmz5+/jSTJ/La2NoqiqP7Y2NhfZ2Vl1ZMkKX3++efMM888Y0tPT88jCCJNJpNl" +
            "+ny+uc8++2x+aWkpDAYDAoHAdOjQ4XAgGAwSoVBoBsuy8a+++urHoVAoMD4+Dp/Ph9mzZ+Opp56CUqmc" +
            "XkpomsaqVasgl8vJysrKVL1eT4iiOH13URQFpVIJuVwubd26lVi2bNn0/6eWinA4jOeffx5Xr16FXq/H" +
            "gQMHQjRNW/x+/w94ntdotVo4HA7YbLYpgwiNRoPS0lJ0d3ev/OUvfxlpMpkmWJZtEEXxU4fD0fj444+P" +
            "3HzzzbQgCERra+vMgYGBpxUKhS0rK0soLCxsSExM/POMGTM+rK+vnxBFEQkJCQDwV9AAIJPJpvbME/n5" +
            "+a83NzcfXrRo0dqmpqZNzc3NRSMjI6+3tLTsFQThk9tvv72b5/m+//iP/7AkJiaqn3zyyS/eeuutVQaD" +
            "YWFGRgaUSiUkSUIoFILD4ZhyfKaMjsbpdN6t0+lgNptBkiTa2trgdruRnJwMgiDA8zxIkoQkSYiNjYUo" +
            "ioRCofjGkKcgCERcXBwoigJJktOglUolhoeH0dbWBqPRCKPRCJIkk10uV7LFYpneb7vdbjgcDpjNZqjV" +
            "aiiVSmRkZECv16sbGhqOb9++veypp57K6e3tHVq1alXf7bffjgsXLsRSFLVBp9PdW1JSYsjJyTmWl5d3" +
            "KCsr6/Ps7OxRQRCQn5+Pu+++e3qcsq8PXC6XAwDi4+NHs7Oz9ycnJ7dlZWX9j5qamrWfffbZ0/39/T8d" +
            "GBhopCiK4ThuXktLy9kjR45ox8fHZ61YsYIyGAyQy+VwuVxob2/H6OgoPB4PFAoFoqOj4Xa7YbFYYDAY" +
            "4Pf7EQqFQJKkR6vV9mi12jySJKlwODxtG/x+//Rt/XXHgCAIKJVK+P1+TF0IuVw+pZmCRqOpUyqVqQqF" +
            "wmgwGKDVakHTNGQyGeLi4jAxMQGPx4PR0VG0t7dj1qxZMBqNU2u7/Jlnnil84403AiRJLp6YmCitrq6u" +
            "FQSBDofDeXFxcbbly5eL8+bNO5qenv6H2bNn13q9Xm7qrvq6fAV0dXU1FAoF6fV67ceOHVt87dq1H9bX" +
            "1y+qr6+39Pb2EizLAoCNZVnbl8YmOD4+Pp+m6Vi9Xj8xMTERaG5u1qnVavT09KC/vx+jo6OQy+WIiYmB" +
            "XC6H3++H0WiEVqsFwzBTVtpTVlb229zc3LkEQeygKCoyHA6D53k4HA7Y7XaoVCrcuHQAAEmSiI6OhsPh" +
            "wOLFiyGXy6f2xh6VSvV6eXl5tVarfYmiKKNCoYDJZIJcLkcgEIDVaoXRaMTY2BhGR0ehVqshk8mmnZ2J" +
            "iYmATqeTT05O/gvLsvEsy+qCweAtU2Po7u7Gq6++Sh49evSWWbNmzc/Ly6tISUk5XFZWVhEZGTkUDofF" +
            "uXPnfjNokiRBEIRKFMUdXV1dP3333XeNHR0dX5mgXq8fUalUn5AkWaNWqx3R0dFDu3btijCbzQuee+45" +
            "9euvv164fPlynVwuRzAYhNVqhdlsBkEQCIfDsFgsGB4eBsMwEEURGo0GoigmXrhw4fnGxsb/+eKLL27T" +
            "arW/kcvlc3t7e9HT04OSkhLQNP2NGp2WloZz587B5/MhMTERNE03chz37/fff7/a7XY/p9FoEimKAs/z" +
            "mJiYQCgUQnR0NDQaDZRKJaKiouByuRAIBBAKhVBfX4+ysrLJ5OTk2ldffbVsfHy84vHHH3e7XK6YUCiU" +
            "JghCYSgU2uDz+Wwsy8LhcBDd3d3WpqamTZs3b15mtVpfFQThWQBfiWN8Rcd37tyJixcvcrGxsZdUKtWV" +
            "+Ph4WVRUlEUul6sFQZgyRhKAcZqmu2QyWQ9JkhORkZFOq9XampSUdOjo0aNdLS0tBTqdzpCbmwubzQaF" +
            "QgGSJEGSJFQqFXiex+TkJBQKxbQbLAiCORQKrfr888/DGo3mudzcXP7gwYN5fr+fnNoTTxm5qSaXy6HT" +
            "6XD9+nV4PB6mtLT0rTNnzrz05JNPrgkEAr+UyWRRer0eKpUKJEmCYRhYLBbExcVBrVZDrVbDYDAgNjYW" +
            "kZGRcDgcOHHixEAgEPjFww8//D9Jkmy8dOnSRHNzs8AwDC0IQlw4HJ7D83yGTCajzWYzMjMzxSVLlgyv" +
            "XLnyaGZm5lMmk+ndlpYWf2JiIl577bX/rRTfZGAkScLY2Bi0Wi3d1taW0tfXt9Ttdv+kubk588iRI+ju" +
            "7gZJkhJFUW6SJIfkcvm4QqFgRVEkeZ6nKYqyKpVKYf78+eklJSVytVo9vaeecsWnjqeM0tQxwzAgCOKK" +
            "RqP5VKPRrA8EAnmZmZmK/Pz8aQM5pc2iKKKhoQGtra1BjUbTFAqFjgYCgR+Iolgw5V5Pre83HtM0PX08" +
            "NbZz585xtbW13cFgkOB5foyiKIaiKJFlWQXHcRZRFO2CIJhEUSSSk5Oxbt065OTktBuNxlfj4uJO5+Tk" +
            "dHs8Hs5ut39jZYDsm0BPdQyHw8zw8HCrzWYT9Hp9nsfjsWu1Wj0AiKJIiKJoBmBmGAYkSQo0TTusVuvp" +
            "DRs2dK5cubKooqIi7fPPP0dhYSESExO/opVTx1/33mQyGfx+f4HD4Zgxd+7c3992223vHj169NGxsbH4" +
            "JUuWwGg0QpIkeL1enDlzBgzD9G/ZsuXVo0ePkk1NTb+JiopSTRm9KcA3gv46/J6eHtTW1qKgoEC6//77" +
            "68vLy2s++OCD5ImJiRUMw3zFKZkSrVaL2NhYX3x8/Bmapk/6/X6HRqPBfyXUf/Xh008/jZ07d8Jut4fb" +
            "2trSqqqqiq9cuUKHQqHpPnK53KnT6Y5bLJan16xZ88ybb77pz87Onq9QKOisrKzqQCBw/JNPPiEFQYhP" +
            "SEggaJoGQRDTS8nXG0EQGB4eBs/z8lAoNK+7u7tn48aNfxofHzdcvnw52WQyER6PBydPnhTtdvvZJUuW" +
            "PH3w4MHcoaGhh8LhsEYURdhsNmg0munl4etNq9WCJElUVVVJ5eXlF5YvX75v/fr1rSqVip49e7Zx27Zt" +
            "p1iWfW5oaOgiQRCEKIpWURSnSfI8D71ez0RHRx8pKCgo53mee+ONN/5L0P9p9cvZs2cRCoWg0+liLl++" +
            "vOujjz666+LFi3JBEKBQKMCyLGia9tpstj/pdLrDBEFcj4+PZ1NTUwlBEKT29nZycHAwIhAIpGo0mvkG" +
            "g+Fmk8mUW1JSQkREREzHQL7e2tvb0d7ePu2l6XQ66PX66sLCwlcpippz5MiRB+x2u7+0tPQNr9fbcfHi" +
            "xQd9Pl/R5OTktDeamZmJzMzMrywRN0bqxsfHce7cOWFoaKiGYZgzBEFcVigUHXq93pmTkyNqNBqqr69P" +
            "un79uiIQCMT5/f6NIyMjOxiGMUzN/cu4Onfrrbfunz179hOTk5ODarUaS5Ys+XagvywTIE6fPv1gRUXF" +
            "E5999lkkx3FETk4O+vv7UV1dDQASQRAsRVEBkiS9FEVN8jw/+aXGGgDYtFrt0MKFCz/Ytm1b8aVLl246" +
            "f/68NHPmTCIrKwscx02npMLhMBoaGqaDQseOHUN7ezvkcjm0Wi3kcrkzEAh0ZGVlMcuWLfv/Tpw4keHx" +
            "eB70eDyWQCAAlmWRlZWFNWvW4NixY1AoFMjLy5tek6eCPa2traivr5eKi4uJuXPnnnzzzTfPXbhw4dZQ" +
            "KBQniuKwKIo+AJDJZDpRFA2CIOgEQdBIkqQAQMyfPx9xcXFobm6GXC6X1q1bN1ZcXLxrxYoVr1dVVUkL" +
            "Fy78dqAvXbqEOXPmEBUVFUqWZZOcTmcxSZIJNE0XHz9+PL27u1sXDAblg4ODsuvXr//N9+VyecBms324" +
            "ZcuWN9evX7/M7/fPJknSce7cuY633377/vz8/KIVK1aApmlMTEzg4sWLAIB7770XdrsdPp8PR44cwcmT" +
            "JzE5OQmDwTA5f/78P5lMpqr6+vod4XB4hdvtJqe0a9WqVVi3bh10Oh2Ghoawd+9eEASBefPmwWKxgGVZ" +
            "nDp1CnV1dRfvueeePxcXFydLkjTDYDC0fPrpp8fffvvte8bGxu7gef5vFtv4+HjExMTwarWaS0lJ8a1e" +
            "vbozGAyWEwQxZLVaLygUip7FixeHL126JM2bN+/bgf66NDU1gSRJ0uVymb50KKxOp/O+F198cWtVVdX/" +
            "/kGCEDUazWW73f77W2+99fjTTz8tbtiwoUAQBLXVar3g8XgSOzs7H5HJZNtUKhWn0+l0XV1dSEpKGnni" +
            "iSci9Hq9XBRFUBQFt9uN3bt3iwMDA7Vbtmx559KlSzqPx/NIIBCw+/1+sCwr+v1+X35+vn7nzp2kyWSC" +
            "IAggSRI+n4/btWvXRE9Pjy0tLQ3j4+MIh8MgCOLlhISEF4PBYG9kZORSmqaD+/btq7n77rtRUVFx08TE" +
            "xG8CgcAcURTJqTktWLAAjz766LsRERFv0DQ9JgjCmEql8oiiKBYWFv5f8ft/qlAsLy+HzWazv/nmm3/Z" +
            "s2fPnBuNo8FgGLBYLLtZlr1is9kQCARSBgcHf24wGNoCgYA8MzNzOC0tTTsyMrLK6XS2tLe3FzAMo8/J" +
            "yenct2+fnqIoO0EQaGlpwYcffhhgGGZfbGzsZ729vfeJovgDn88n53keXq8Xoii6Z82a9etAIJCh0+l2" +
            "bNq0ic7OzoYkSRAEYfhHP/qRr66uLkOhUMButyM+Ph5ZWVnvNzQ0ODs7O+00TTPBYDAnPj7+BZqmHSMj" +
            "I1AoFAXj4+O/8Hq9sVNzUqvVeOCBB65s37593cjIyFBpaem3ZkZ92y+cP38e99xzD1JSUn78/vvvbx0Y" +
            "GPjK5wzD6Lxe73K/37/Z6XTeNTExsSEYDEb7fL4cs9k8UVpa2tzd3b01HA5bQ6FQw89//vPnWZadd+nS" +
            "JWtRUVFPTEyM/fPPPycOHDjQpFAofqXRaDyjo6O/EwSheHJykhIEAaOjo6AoComJiSpJktJMJtOxyMjI" +
            "qrKysjSO44wZGRlSXV1d65tvvploNpuVubm5CIfDUCqVQiAQSMzKyjrV0dGRNTo6ui4cDke5XK61Y2Nj" +
            "d3q93q1ut/smhmGMNyohx3EIhUL2mJgY586dOyuPHDmCvXv3fitusm/V+68g8eGHHyZWVFRsVSgUyMzM" +
            "BPDXYI7RaBTUajVVWVkp9/v98qnszJea3mo0Gi+3tbXdv3TpUrNSqURlZeXq6upqVSgUMvA8r9+9e7e5" +
            "oKDA43A4TqalpR10Op3rgsHgZoZhlAzDQBAEREdHw2KxwOl0IhwOQ5KkdL/fv9vv97+fm5v76/Pnz9/V" +
            "1tY29/LlyxE8zxs4jsPo6Ciys7NRUlJChUIhfVlZ2Xar1XpIEAT7+Ph4BsdxsikWWq0WCxcuRDAYFDwe" +
            "DzUV3JLJZHC5XFs/+OCDD7xeb++35fatQRMEgcnJSW9aWtpjP//5z+2CIBgBEHK53K1QKJbu3bv3jq+X" +
            "K2g0Gt5utyvy8vLuX79+vSY6OhrXr1+H0WhUfvzxxysnJydJgiDQ29tLRUREPJOYmOhyuVy/o2k6b2Ji" +
            "ApIkged5rFy5EnfeeScA4N1330VZWRkoioLX61WazeYfdXR0zLZara/5fL7a8fHxBwiCgNfrBcdxKC4u" +
            "RlRUFBISEpCWlhb32Wef3c9xXH8oFOICgYB8aqxTkcAdO3a8z3HcGY7jzAAkiqI8Go1maHJy0vv/UhP+" +
            "nVSRHz16FEajMeOdd975dN++fZkMw3zl85SUFGzevHk6Az4VNx4eHsbZs2dx6dIlKRAIVGRmZr4SCoXm" +
            "q9XqbRzH6d1uNwBAoVBw9913H1VcXEwSBAFJkiBJEiorK/H2229PJxnMZjPkcvkkz/NvG43G2qqqqq0G" +
            "g2F5YWEhUVpaCpvNBp7nAWA6E/7uu++iu7v7K+OlaRpbt25t3bp16wav19t58803/92MvvUa/XWpqKhA" +
            "fHy8squr65m2trYVMpkMIyMjkCQJMpkMxcXFePjhh1FQUAC5XA6ZTDadvIyPj0dJSYlr5cqVr4+Ojp50" +
            "u90/tlgst/t8Ptrn84FhGI4giPInnniifMGCBZckSSoHUEUQRANFUe2pqakNWVlZ/Q0NDbzH4zFyHEcR" +
            "BEGbzeZ5ExMTkRkZGW8988wz3cuWLcuyWCwqURSnM+IURSEpKQm5ubnwer0YGBiY/n9BQQFMJpPVZrMp" +
            "s7Ozv9i0aZOwb9++7xf0tm3bEA6HjRRFKWNiYtRXrlxJ6evrAwCsW7cOjzzyCGw2GwiCgEwmA8/zOHTo" +
            "EE6dOsVOTk46ATz97LPPUqIo/io6OnrGtWvXEAgE0NfXh2vXrrmNRuNbAwMDr0VHR3/c0NBwasGCBad3" +
            "7959jGGYT8bGxj6rqqq60NfXR9TV1RWwLKtSKpUIBoNESkpKvNfrLT5x4kR1cnLyqaqqqnkff/yxrKen" +
            "h0pPT4dcLockSTCbzZg3bx4mJyfR3t4OSZKQmJiIzZs3nzKbzUfC4XCvJEmhvxf0t16jvy5qtRr19fXj" +
            "UVFRA4cPH87/0mMEAPh8vukQKUEQ0Gg0KCsrw2uvvRbKz89/cXR0NPnll1/elpGRMSMtLU1WVVWFUCiE" +
            "oaEh9Pb2giRJS11d3R9aWlruO3LkyCWFQtGWmJjoBoD333/fxHHcjHA4PJdl2XQA9OTkJCYnJ5GWlob2" +
            "9nYsXLjQWlNT86sdO3a0p6amnvb7/dc++uijh2JjY9XLly/H5OQkAEChUMDn802Pu7q6Gna7PXfr1q2D" +
            "fX19rhsD+N8b6LGxMWRkZKRVVVXtbmhoiDKZTJDJZKBpGsPDw3j//fdB0zQyMjKwfv16aXx8nFcqlX1F" +
            "RUV7r1y5ku71eve4XC6Zy+WCyWQCTdNISUmB1+vFTTfdhLGxMUV5eXkuwzC5JElifHwcAKZDpoIgYO7c" +
            "uUhOTsbJkydht9tBURRMJhMmJibgdrtlXq/XGAgE3s7Kympvbm6+2el0phoMBsXZs2eJjo4OMAyD4eFh" +
            "JCQkgGEY8DyPhoYGW1tb2/MLFy68dXR01PG9gwaAUCiktdvtRx588MEqjuMohULBmUym5MuXL9/03nvv" +
            "EePj49i+fTu3ZcuW9wYGBtwGg+HegwcPvuFyuXJ5no8YHx+fjjP39/cjJiYG//qv/4qkpCTU1NTgzJkz" +
            "SElJQVRUFK5cuQKFQoHt27fj2rVrOHToENLT07Fw4ULExMSgr68PHR0dsNvtAACn0wmXyxVbW1v7TldX" +
            "V3NERER8T0/PSzqdLrKlpWXzn/70J5nFYsGdd94pzZ0797jL5brGcZxCJpMJkZGRE6FQSPtdPHn2d4Ne" +
            "tWoVANR92XDu3DnQNB3tcrleSExM5KOjo+U8z+Ps2bPDO3bs6BoaGtpsMpn0Op2udGpXIUkSDAbDlDES" +
            "y8rKyKtXr2Lu3LkwmUzYtGkTtFotTAYjtBoNOI5HyeISzJgxAxqNBlarFVevXkVtbS0CgQDmzJnDm81m" +
            "mcFguDFREBEZGVkil8vR39+/7sEHH3yvsbFx1Gg0xkRHRyMhIUGwWCzh1NTUZ1iWHS4pKfkudPC7A/11" +
            "4TgOCoVCVCqV5yIiIjJvuummQExMjKe8vHxxQ0PDU0lJSaRer59+PqapqQlZWVmIiIiAyWRCOBwmjUYj" +
            "mpubEQgE0NraCplMBibMSKIgEpmZmeB5AZ8f+xxutxsejwd9fX1QqVTgOA4rVqxATEyMTKlUgiRJZGVl" +
            "IRgMTkfyOI6DKIoZdXV1v83IyAjt2LHj+ODgoNFisWhpmj7D8zx/Y3XWPy3o/Px8WK3W0bNnzx5KSkr6" +
            "bOvWrRNnzpxZGwwG51y7di0QDAYjU1NToVAoQBAEZsyYgalKIa1WC4IgMDAwAJZlMXfuXGRlZYlDQ0Pv" +
            "paSkmAmCWONyucCyLAiCQHR0NFQqFSiKwvj4OKbCAVNPFvj9fixatAgpKSlgWRaSJIFlWXR1dYEgiP6i" +
            "oiJVXl7e3kceeeTzCxcuRADgFy1aND42NvbPD9pqtQIASktLR69cuQIABM/z8UVFRU+RJBlx6NChnQ6H" +
            "Q5+Tk4O4uLjpTIhGo5lOtPb09GDmzJmQRBG0QvFOZmbmo7fddtsvGIZZM5V79Pl88Hq908XpSqUSCQkJ" +
            "aG1tRW5uLuLj46cTuCqVCn6/HwMDA3A4HOA4bvKOO+7496KiInAcFw8grFKpBubMmfOdA54S8u//if9c" +
            "5HI5WltbCY1Gc4Rl2YzJycmH3G63rra2Fn19fdBqtTAYDNNVQhUVFTh27BgWLVyIglmzIQriWVpBP75o" +
            "4SIPx3HOqbo8QRCmvUOCIKYLGjMyMqDVavHGG2/g3Llz0Gg0MJvNMBgM0Ov1uH79OmprazExMaFlWXYX" +
            "x3EztFrtkebmZmKqcOgfJd+5Rt8oeXl5uHDhgpient7V2Nj4UlRUlCwpKWmz2WzWr1mzBiaTCTqdDh6P" +
            "B8ePHxdUShV52w9vJQBgcnJyhKbp3wQCgRFrpBUEQbgATIO9sd2Yc4yKigLP8/j000/R1dUlbty4kfzS" +
            "NceaNWswMTEBvV4/abPZPpLJZC/l5OR0Nzc3/6eZke9K/qEaDQCLFi1CZGSklJCQ0PXQQw/9bM2aNXfw" +
            "PN/CMAyMRiMaGxvx7LPPQhCE67fffttVjUbjCvgDEEXxswd+/GBVVnbWVFZ+lCAIZgrs1wHfAF4ymUzu" +
            "kpKSlmAwOPjss8+isbERRqNxqmin7ZZbbtny0EMP/SwhIaHLYDD8wyED34EL/n8rL7/8MkZGRoTf//73" +
            "DkEQusbHx5f09/drz58/L/b29pJut9vY0dkpUyjo6yaT8brZbN6Xm5fXtv/AAQSDQRAEYRBF8U5BEJRT" +
            "T2vd8IQWJ4ripFwuHzAYDH0EQXi7u7ujHQ5H1PDwMOF0OoXR0VHU1NQM9Pb2/uSjjz46ZrfbhZUrV/53" +
            "Tf+7id59G5kxYwaio6OJ9vb2Qq1Wm/jII49onU7nFpZl57355psqvU6HyMjIgNVqvRafkHA5Njb2alxc" +
            "XEdGRoZgtVp/yXFcRDgclkKhkNLn83Eul4sbGxsjrl+/Lu/s7DSOjIxEOp1Otdfrxf333x9SKBS1UVFR" +
            "773wwgs+v9/fm5WVVTM0NCS1tLT8t877e33ZhiRJeO655/Czn/1Mv3///tdOnDhx+5IlS7yVlZUR+/fv" +
            "R1JSEmJiYuDz+Ri5XD5pMBgYiqLCACCKotrn89GhUIjW6/XKoaEhqre3F3fddRcWLlw4cebMGeOqVave" +
            "37p1649feukl36OPPvq9vlvkH2oM/08ylRukKGrSYrFcv/322//NYDB4T5w48fqsmTNl923bBpVKhV27" +
            "dtGjo6P0nXfeialYN03TeO+99xAVFYVHH30UoVAIf/7znzE5Ocmnp6f/D5vNZqQoyiiTyXwNDQ3f+wtc" +
            "vlfQAKBSqdDU1EQYjcZXFi1aNPbOO+88a7fbubVr1n6QlJzk7unp2RwOh03r1q3Dhg0bgoIg9AAARVFJ" +
            "fr9fXVFRAVEUXZmZmQd+8pOfRNTX128aGBjIvOuuu35ZWVlpvXr1KqnT6cS/d5x/r/zTvKfn1KlTIEky" +
            "KRgMviEIwnvr160/0NjUGPXWW2+d7unpybjrrrtGjUbj0wqF4rMvS4Bv9nq9T+zfvz8qKSnJcd999y3L" +
            "zs4ePXr06GaSJDdrtdrtoiheW758+fc9NQD/jbuO/5PcddddkMvl8RRFVaxevfrYx4c+Fvx+/4ZwOHzP" +
            "woULr0RFRT2YkJDwEcdxHoqivHa7/QrP81cSExOzJEnKomm6qaWl5eqGDRsaBgcHO2QyGSUIwuj+/fu/" +
            "76kB+CfSaEmSiLq6OkIQBCkQCEgEQWj8fv8BAAGtVvvrcDjca7fbkZubC+CvL9Tq7e2FSqVK8Pv9vyNJ" +
            "UqfT6baIohhQq9UERVHErFmzJIIg/jEvfPqW8k8D+kYpLy8HgFye51dSFPWmKIq+FStWfGPfU6dOgaIo" +
            "Pc/z98vl8i8ANP1nhYbfp/wvgQ4FRPoxsbQAAAAtdEVYdGljYzpjb3B5cmlnaHQAQ29weXJpZ2h0IEFy" +
            "dGlmZXggU29mdHdhcmUgMjAxMQi6xbQAAAAxdEVYdGljYzpkZXNjcmlwdGlvbgBBcnRpZmV4IFNvZnR3" +
            "YXJlIHNSR0IgSUNDIFByb2ZpbGUTDAGGAAAAAElFTkSuQmCC";
            byte[] logoBytes = java.util.Base64.getDecoder().decode(logoBase64);
            java.awt.image.BufferedImage logoImg = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(logoBytes));
            Image scaledLogo = logoImg.getScaledInstance(90, 55, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(scaledLogo));
            lblLogo.setBounds(205, 10, 90, 55);
            panel.add(lblLogo);
        } catch (Exception ex) { ex.printStackTrace(); }

        JLabel lblTitle = new JLabel("MotorShop", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 68, 500, 30);
        panel.add(lblTitle);

        JLabel lblSub = new JLabel("Parts Ordering System  \u00b7  Staff Login", SwingConstants.CENTER);
        lblSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblSub.setForeground(new Color(100, 100, 100));
        lblSub.setBounds(0, 98, 500, 18);
        panel.add(lblSub);

        JPanel divider = new JPanel();
        divider.setBackground(new Color(42, 42, 42));
        divider.setBounds(60, 126, 380, 1);
        panel.add(divider);

        // USERNAME
        JLabel lblUser = makeFieldLabel("USERNAME");
        lblUser.setBounds(60, 146, 200, 16);
        panel.add(lblUser);
        txtUsername = new JTextField();
        styleField(txtUsername);
        txtUsername.setBounds(60, 164, 380, 36);
        panel.add(txtUsername);

        // PASSWORD
        JLabel lblPass = makeFieldLabel("PASSWORD");
        lblPass.setBounds(60, 212, 200, 16);
        panel.add(lblPass);
        txtPassword = new JPasswordField();
        styleField(txtPassword);
        txtPassword.setBounds(60, 230, 380, 36);
        panel.add(txtPassword);

        JCheckBox chkShow = new JCheckBox("Show password");
        chkShow.setFont(new Font("SansSerif", Font.PLAIN, 11));
        chkShow.setForeground(new Color(100, 100, 100));
        chkShow.setBackground(new Color(17, 17, 17));
        chkShow.setBounds(60, 272, 150, 22);
        chkShow.addActionListener(e ->
            txtPassword.setEchoChar(chkShow.isSelected() ? (char) 0 : '\u2022'));
        panel.add(chkShow);

        // SECURITY
        JLabel lblSec = makeFieldLabel("SECURITY ANSWER");
        lblSec.setBounds(60, 304, 200, 16);
        panel.add(lblSec);
        txtSecurity = new JTextField();
        styleField(txtSecurity);
        txtSecurity.setBounds(60, 322, 380, 36);
        panel.add(txtSecurity);

        // BUTTONS
        JButton btnLogin = makeOrangeButton("LOGIN");
        btnLogin.setBounds(60, 378, 160, 38);
        btnLogin.addActionListener(e -> doLogin());
        panel.add(btnLogin);

        JButton btnClear = makeDarkButton("CLEAR");
        btnClear.setBounds(234, 378, 90, 38);
        btnClear.addActionListener(e -> clearFields());
        panel.add(btnClear);

        JButton btnRegister = makeDarkButton("Register");
        btnRegister.setBounds(338, 378, 102, 38);
        btnRegister.addActionListener(e -> doRegister());
        panel.add(btnRegister);

        JLabel lblForgot = new JLabel("Forgot password?");
        lblForgot.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblForgot.setForeground(new Color(255, 140, 0));
        lblForgot.setBounds(188, 428, 130, 20);
        lblForgot.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblForgot.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { doForgot(); }
            public void mouseEntered(MouseEvent e) { lblForgot.setForeground(new Color(255, 180, 60)); }
            public void mouseExited(MouseEvent e)  { lblForgot.setForeground(new Color(255, 140, 0)); }
        });
        panel.add(lblForgot);
    }

    // ── MAG-CHECK SA DATABASE KUNG VALIDO ANG ACCOUNT ───────────────────────────────────────────
    private void doLogin() {
        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword()).trim();
        String sec  = txtSecurity.getText().trim();

        if (user.isEmpty() || pass.isEmpty() || sec.isEmpty()) {
            showError("Please fill in all fields."); return;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND security_answer = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user);
            stmt.setString(2, pass);
            stmt.setString(3, sec);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(frame,
                    "Welcome back, " + user + "!",
                    "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                frame.setVisible(false);
                frame.dispose();
                SwingUtilities.invokeLater(() -> {
                    HomePanel home = new HomePanel(user);
                    home.showWindow();
                });
            } else {
                showError("Invalid username, password, or security answer.");
            }

        } catch (SQLException e) {
            showError("Database connecting error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── MAG-SAVE NG BAGONG ACCOUNT DIRETSO SA DATABASE ────────────────────────────────────────────────
    private void doRegister() {
        JTextField newUser = new JTextField();
        JPasswordField newPass = new JPasswordField();
        JTextField newSec = new JTextField();

        JPanel form = new JPanel(new GridLayout(6, 1, 4, 4));
        form.add(makeFieldLabel("New Username:")); form.add(newUser);
        form.add(makeFieldLabel("New Password:")); form.add(newPass);
        form.add(makeFieldLabel("Security Answer:")); form.add(newSec);

        int opt = JOptionPane.showConfirmDialog(frame, form,
            "Create Account", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (opt == JOptionPane.OK_OPTION) {
            String u = newUser.getText().trim();
            String p = new String(newPass.getPassword()).trim();
            String s = newSec.getText().trim();

            if (u.isEmpty() || p.isEmpty() || s.isEmpty()) {
                showError("All fields are required."); return;
            }

            String insertSQL = "INSERT INTO users (username, password, security_answer) VALUES (?, ?, ?)";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

                stmt.setString(1, u);
                stmt.setString(2, p);
                stmt.setString(3, s);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(frame,
                        "Account created and securely saved to Database! You can now log in.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                showError("Could not save account. (Username might already exist)");
                e.printStackTrace();
            }
        }
    }

    // ── CORRESPONDING FORGOT PASSWORD CHECKS SA DB ────────────────────────────────────────────────
    private void doForgot() {
        String user = JOptionPane.showInputDialog(frame, "Enter your username to recover:");
        if (user == null || user.trim().isEmpty()) return;

        String answer = JOptionPane.showInputDialog(frame, "Enter your security answer:");
        if (answer == null || answer.trim().isEmpty()) return;

        String query = "SELECT password FROM users WHERE username = ? AND security_answer = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.trim());
            stmt.setString(2, answer.trim());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(frame,
                    "Your password is: " + rs.getString("password"),
                    "Password Recovery", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showError("Incorrect username or security answer.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtUsername.setText("");
        txtPassword.setText("");
        txtSecurity.setText("");
        txtUsername.requestFocus();
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(frame, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // ── UI helpers ────────────────────────────────────────────────────────────────────────────
    public JLabel makeFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 10));
        l.setForeground(new Color(255, 140, 0));
        return l;
    }

    public void styleField(JTextField f) {
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setForeground(Color.WHITE);
        f.setBackground(new Color(26, 26, 26));
        f.setCaretColor(new Color(255, 140, 0));
        f.setBorder(new CompoundBorder(
            new LineBorder(new Color(52, 52, 52), 1),
            new EmptyBorder(6, 10, 6, 10)));
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                    new LineBorder(new Color(255, 140, 0), 1),
                    new EmptyBorder(6, 10, 6, 10)));
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(new CompoundBorder(
                    new LineBorder(new Color(52, 52, 52), 1),
                    new EmptyBorder(6, 10, 6, 10)));
            }
        });
    }

    public JButton makeOrangeButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(new Color(30, 15, 0));
        btn.setBackground(new Color(255, 140, 0));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(255, 165, 40)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(255, 140, 0)); }
        });
        return btn;
    }

    public JButton makeDarkButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(new Color(34, 34, 34));
        btn.setOpaque(true);
        btn.setBorder(new LineBorder(new Color(42, 42, 42), 1));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(50,50,50)); btn.setBorder(new LineBorder(new Color(255,140,0),1)); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(new Color(34,34,34)); btn.setBorder(new LineBorder(new Color(42,42,42),1)); }
        });
        return btn;
    }
}