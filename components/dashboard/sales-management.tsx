"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Plus, ShoppingCart, Trash2, Receipt } from "lucide-react"

interface CartItem {
  id: string
  name: string
  price: number
  quantity: number
  stock: number
}

interface Sale {
  id: string
  date: string
  customer: string
  items: CartItem[]
  total: number
  status: string
}

export function SalesManagement() {
  const [cart, setCart] = useState<CartItem[]>([])
  const [customerName, setCustomerName] = useState("")
  const [isNewSaleDialogOpen, setIsNewSaleDialogOpen] = useState(false)
  const [selectedMedicine, setSelectedMedicine] = useState("")
  const [quantity, setQuantity] = useState(1)

  const [sales, setSales] = useState<Sale[]>([
    {
      id: "INV-001",
      date: "2024-01-15",
      customer: "John Doe",
      items: [{ id: "MED001", name: "Paracetamol 500mg", price: 2.5, quantity: 2, stock: 150 }],
      total: 5.0,
      status: "Completed",
    },
    {
      id: "INV-002",
      date: "2024-01-15",
      customer: "Jane Smith",
      items: [
        { id: "MED002", name: "Amoxicillin 250mg", price: 15.75, quantity: 1, stock: 8 },
        { id: "MED003", name: "Ibuprofen 400mg", price: 3.25, quantity: 2, stock: 75 },
      ],
      total: 22.25,
      status: "Completed",
    },
  ])

  const availableMedicines = [
    { id: "MED001", name: "Paracetamol 500mg", price: 2.5, stock: 150 },
    { id: "MED002", name: "Amoxicillin 250mg", price: 15.75, stock: 8 },
    { id: "MED003", name: "Ibuprofen 400mg", price: 3.25, stock: 75 },
    { id: "MED004", name: "Aspirin 325mg", price: 1.75, stock: 200 },
    { id: "MED005", name: "Cough Syrup", price: 8.5, stock: 45 },
  ]

  const addToCart = () => {
    const medicine = availableMedicines.find((m) => m.id === selectedMedicine)
    if (medicine && quantity > 0 && quantity <= medicine.stock) {
      const existingItem = cart.find((item) => item.id === medicine.id)
      if (existingItem) {
        setCart(cart.map((item) => (item.id === medicine.id ? { ...item, quantity: item.quantity + quantity } : item)))
      } else {
        setCart([
          ...cart,
          {
            id: medicine.id,
            name: medicine.name,
            price: medicine.price,
            quantity: quantity,
            stock: medicine.stock,
          },
        ])
      }
      setSelectedMedicine("")
      setQuantity(1)
    }
  }

  const removeFromCart = (id: string) => {
    setCart(cart.filter((item) => item.id !== id))
  }

  const getCartTotal = () => {
    return cart.reduce((total, item) => total + item.price * item.quantity, 0)
  }

  const completeSale = () => {
    if (cart.length > 0 && customerName) {
      const newSale: Sale = {
        id: `INV-${String(sales.length + 1).padStart(3, "0")}`,
        date: new Date().toISOString().split("T")[0],
        customer: customerName,
        items: [...cart],
        total: getCartTotal(),
        status: "Completed",
      }
      setSales([newSale, ...sales])
      setCart([])
      setCustomerName("")
      setIsNewSaleDialogOpen(false)
    }
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="flex items-center gap-2">
                <ShoppingCart className="h-5 w-5" />
                Sales Management
              </CardTitle>
              <CardDescription>Process sales and manage transactions</CardDescription>
            </div>
            <Dialog open={isNewSaleDialogOpen} onOpenChange={setIsNewSaleDialogOpen}>
              <DialogTrigger asChild>
                <Button>
                  <Plus className="h-4 w-4 mr-2" />
                  New Sale
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-[600px]">
                <DialogHeader>
                  <DialogTitle>New Sale</DialogTitle>
                  <DialogDescription>Add medicines to cart and complete the sale.</DialogDescription>
                </DialogHeader>
                <div className="space-y-4">
                  {/* Customer Info */}
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="customer" className="text-right">
                      Customer
                    </Label>
                    <Input
                      id="customer"
                      value={customerName}
                      onChange={(e) => setCustomerName(e.target.value)}
                      placeholder="Customer name"
                      className="col-span-3"
                    />
                  </div>

                  {/* Add Medicine to Cart */}
                  <div className="border rounded-lg p-4 space-y-4">
                    <h4 className="font-medium">Add Medicine</h4>
                    <div className="flex gap-2">
                      <Select value={selectedMedicine} onValueChange={setSelectedMedicine}>
                        <SelectTrigger className="flex-1">
                          <SelectValue placeholder="Select medicine" />
                        </SelectTrigger>
                        <SelectContent>
                          {availableMedicines.map((medicine) => (
                            <SelectItem key={medicine.id} value={medicine.id}>
                              {medicine.name} - ${medicine.price} (Stock: {medicine.stock})
                            </SelectItem>
                          ))}
                        </SelectContent>
                      </Select>
                      <Input
                        type="number"
                        min="1"
                        value={quantity}
                        onChange={(e) => setQuantity(Number.parseInt(e.target.value) || 1)}
                        className="w-20"
                      />
                      <Button onClick={addToCart} disabled={!selectedMedicine}>
                        Add
                      </Button>
                    </div>
                  </div>

                  {/* Cart */}
                  {cart.length > 0 && (
                    <div className="border rounded-lg">
                      <Table>
                        <TableHeader>
                          <TableRow>
                            <TableHead>Medicine</TableHead>
                            <TableHead>Price</TableHead>
                            <TableHead>Qty</TableHead>
                            <TableHead>Total</TableHead>
                            <TableHead>Action</TableHead>
                          </TableRow>
                        </TableHeader>
                        <TableBody>
                          {cart.map((item) => (
                            <TableRow key={item.id}>
                              <TableCell>{item.name}</TableCell>
                              <TableCell>${item.price.toFixed(2)}</TableCell>
                              <TableCell>{item.quantity}</TableCell>
                              <TableCell>${(item.price * item.quantity).toFixed(2)}</TableCell>
                              <TableCell>
                                <Button variant="ghost" size="sm" onClick={() => removeFromCart(item.id)}>
                                  <Trash2 className="h-4 w-4" />
                                </Button>
                              </TableCell>
                            </TableRow>
                          ))}
                          <TableRow>
                            <TableCell colSpan={3} className="font-medium">
                              Total
                            </TableCell>
                            <TableCell className="font-bold">${getCartTotal().toFixed(2)}</TableCell>
                            <TableCell></TableCell>
                          </TableRow>
                        </TableBody>
                      </Table>
                    </div>
                  )}
                </div>
                <DialogFooter>
                  <Button onClick={completeSale} disabled={cart.length === 0 || !customerName}>
                    Complete Sale
                  </Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>
        </CardHeader>
        <CardContent>
          {/* Sales History */}
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Invoice ID</TableHead>
                  <TableHead>Date</TableHead>
                  <TableHead>Customer</TableHead>
                  <TableHead>Items</TableHead>
                  <TableHead>Total</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {sales.map((sale) => (
                  <TableRow key={sale.id}>
                    <TableCell className="font-medium">{sale.id}</TableCell>
                    <TableCell>{sale.date}</TableCell>
                    <TableCell>{sale.customer}</TableCell>
                    <TableCell>{sale.items.length} items</TableCell>
                    <TableCell>${sale.total.toFixed(2)}</TableCell>
                    <TableCell>
                      <Badge variant="default">{sale.status}</Badge>
                    </TableCell>
                    <TableCell>
                      <Button variant="ghost" size="sm">
                        <Receipt className="h-4 w-4" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
