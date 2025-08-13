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
import { Plus, Search, Edit, Trash2, Package } from "lucide-react"

interface Medicine {
  id: string
  name: string
  category: string
  stock: number
  price: number
  expiryDate: string
  supplier: string
  threshold: number
}

export function InventoryManagement() {
  const [medicines, setMedicines] = useState<Medicine[]>([
    {
      id: "MED001",
      name: "Paracetamol 500mg",
      category: "Pain Relief",
      stock: 150,
      price: 2.5,
      expiryDate: "2025-12-31",
      supplier: "PharmaCorp",
      threshold: 20,
    },
    {
      id: "MED002",
      name: "Amoxicillin 250mg",
      category: "Antibiotics",
      stock: 8,
      price: 15.75,
      expiryDate: "2025-06-15",
      supplier: "MediSupply",
      threshold: 25,
    },
    {
      id: "MED003",
      name: "Ibuprofen 400mg",
      category: "Pain Relief",
      stock: 75,
      price: 3.25,
      expiryDate: "2026-03-20",
      supplier: "PharmaCorp",
      threshold: 30,
    },
  ])

  const [searchTerm, setSearchTerm] = useState("")
  const [selectedCategory, setSelectedCategory] = useState("all")
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [newMedicine, setNewMedicine] = useState<Partial<Medicine>>({})

  const categories = ["Pain Relief", "Antibiotics", "Vitamins", "Cardiovascular", "Respiratory"]

  const filteredMedicines = medicines.filter((medicine) => {
    const matchesSearch =
      medicine.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      medicine.id.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesCategory = selectedCategory === "all" || medicine.category === selectedCategory
    return matchesSearch && matchesCategory
  })

  const handleAddMedicine = () => {
    if (newMedicine.name && newMedicine.category && newMedicine.stock && newMedicine.price) {
      const medicine: Medicine = {
        id: `MED${String(medicines.length + 1).padStart(3, "0")}`,
        name: newMedicine.name,
        category: newMedicine.category,
        stock: newMedicine.stock,
        price: newMedicine.price,
        expiryDate: newMedicine.expiryDate || "2025-12-31",
        supplier: newMedicine.supplier || "Unknown",
        threshold: newMedicine.threshold || 20,
      }
      setMedicines([...medicines, medicine])
      setNewMedicine({})
      setIsAddDialogOpen(false)
    }
  }

  const getStockStatus = (stock: number, threshold: number) => {
    if (stock === 0) return { label: "Out of Stock", variant: "destructive" as const }
    if (stock <= threshold) return { label: "Low Stock", variant: "secondary" as const }
    return { label: "In Stock", variant: "default" as const }
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle className="flex items-center gap-2">
                <Package className="h-5 w-5" />
                Inventory Management
              </CardTitle>
              <CardDescription>Manage your medicine inventory</CardDescription>
            </div>
            <Dialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen}>
              <DialogTrigger asChild>
                <Button>
                  <Plus className="h-4 w-4 mr-2" />
                  Add Medicine
                </Button>
              </DialogTrigger>
              <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>Add New Medicine</DialogTitle>
                  <DialogDescription>Enter the details of the new medicine to add to inventory.</DialogDescription>
                </DialogHeader>
                <div className="grid gap-4 py-4">
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="name" className="text-right">
                      Name
                    </Label>
                    <Input
                      id="name"
                      value={newMedicine.name || ""}
                      onChange={(e) => setNewMedicine({ ...newMedicine, name: e.target.value })}
                      className="col-span-3"
                    />
                  </div>
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="category" className="text-right">
                      Category
                    </Label>
                    <Select onValueChange={(value) => setNewMedicine({ ...newMedicine, category: value })}>
                      <SelectTrigger className="col-span-3">
                        <SelectValue placeholder="Select category" />
                      </SelectTrigger>
                      <SelectContent>
                        {categories.map((category) => (
                          <SelectItem key={category} value={category}>
                            {category}
                          </SelectItem>
                        ))}
                      </SelectContent>
                    </Select>
                  </div>
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="stock" className="text-right">
                      Stock
                    </Label>
                    <Input
                      id="stock"
                      type="number"
                      value={newMedicine.stock || ""}
                      onChange={(e) => setNewMedicine({ ...newMedicine, stock: Number.parseInt(e.target.value) })}
                      className="col-span-3"
                    />
                  </div>
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="price" className="text-right">
                      Price
                    </Label>
                    <Input
                      id="price"
                      type="number"
                      step="0.01"
                      value={newMedicine.price || ""}
                      onChange={(e) => setNewMedicine({ ...newMedicine, price: Number.parseFloat(e.target.value) })}
                      className="col-span-3"
                    />
                  </div>
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="expiry" className="text-right">
                      Expiry Date
                    </Label>
                    <Input
                      id="expiry"
                      type="date"
                      value={newMedicine.expiryDate || ""}
                      onChange={(e) => setNewMedicine({ ...newMedicine, expiryDate: e.target.value })}
                      className="col-span-3"
                    />
                  </div>
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="supplier" className="text-right">
                      Supplier
                    </Label>
                    <Input
                      id="supplier"
                      value={newMedicine.supplier || ""}
                      onChange={(e) => setNewMedicine({ ...newMedicine, supplier: e.target.value })}
                      className="col-span-3"
                    />
                  </div>
                  <div className="grid grid-cols-4 items-center gap-4">
                    <Label htmlFor="threshold" className="text-right">
                      Min Stock
                    </Label>
                    <Input
                      id="threshold"
                      type="number"
                      value={newMedicine.threshold || ""}
                      onChange={(e) => setNewMedicine({ ...newMedicine, threshold: Number.parseInt(e.target.value) })}
                      className="col-span-3"
                    />
                  </div>
                </div>
                <DialogFooter>
                  <Button onClick={handleAddMedicine}>Add Medicine</Button>
                </DialogFooter>
              </DialogContent>
            </Dialog>
          </div>
        </CardHeader>
        <CardContent>
          {/* Filters */}
          <div className="flex gap-4 mb-6">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  placeholder="Search medicines..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-8"
                />
              </div>
            </div>
            <Select value={selectedCategory} onValueChange={setSelectedCategory}>
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="All Categories" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Categories</SelectItem>
                {categories.map((category) => (
                  <SelectItem key={category} value={category}>
                    {category}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {/* Medicine Table */}
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>ID</TableHead>
                  <TableHead>Name</TableHead>
                  <TableHead>Category</TableHead>
                  <TableHead>Stock</TableHead>
                  <TableHead>Price</TableHead>
                  <TableHead>Expiry Date</TableHead>
                  <TableHead>Status</TableHead>
                  <TableHead>Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredMedicines.map((medicine) => {
                  const status = getStockStatus(medicine.stock, medicine.threshold)
                  return (
                    <TableRow key={medicine.id}>
                      <TableCell className="font-medium">{medicine.id}</TableCell>
                      <TableCell>{medicine.name}</TableCell>
                      <TableCell>{medicine.category}</TableCell>
                      <TableCell>{medicine.stock}</TableCell>
                      <TableCell>${medicine.price.toFixed(2)}</TableCell>
                      <TableCell>{medicine.expiryDate}</TableCell>
                      <TableCell>
                        <Badge variant={status.variant}>{status.label}</Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex gap-2">
                          <Button variant="ghost" size="sm">
                            <Edit className="h-4 w-4" />
                          </Button>
                          <Button variant="ghost" size="sm">
                            <Trash2 className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  )
                })}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
