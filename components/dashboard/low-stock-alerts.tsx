"use client"

import { useState } from "react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Badge } from "@/components/ui/badge"
import { Alert, AlertDescription } from "@/components/ui/alert"
import { AlertTriangle, CheckCircle, X, Package, TrendingDown } from "lucide-react"

interface LowStockAlert {
  id: string
  medicineId: string
  medicineName: string
  currentStock: number
  threshold: number
  category: string
  supplier: string
  lastRestocked: string
  priority: "High" | "Medium" | "Low"
  status: "Active" | "Dismissed"
}

export function LowStockAlerts() {
  const [alerts, setAlerts] = useState<LowStockAlert[]>([
    {
      id: "ALT001",
      medicineId: "MED002",
      medicineName: "Amoxicillin 250mg",
      currentStock: 8,
      threshold: 25,
      category: "Antibiotics",
      supplier: "MediSupply",
      lastRestocked: "2024-01-10",
      priority: "High",
      status: "Active",
    },
    {
      id: "ALT002",
      medicineId: "MED005",
      medicineName: "Paracetamol 500mg",
      currentStock: 5,
      threshold: 20,
      category: "Pain Relief",
      supplier: "PharmaCorp",
      lastRestocked: "2024-01-12",
      priority: "High",
      status: "Active",
    },
    {
      id: "ALT003",
      medicineId: "MED007",
      medicineName: "Ibuprofen 400mg",
      currentStock: 12,
      threshold: 30,
      category: "Pain Relief",
      supplier: "PharmaCorp",
      lastRestocked: "2024-01-08",
      priority: "Medium",
      status: "Active",
    },
    {
      id: "ALT004",
      medicineId: "MED010",
      medicineName: "Cough Syrup",
      currentStock: 15,
      threshold: 25,
      category: "Respiratory",
      supplier: "HealthCare Distributors",
      lastRestocked: "2024-01-05",
      priority: "Medium",
      status: "Active",
    },
    {
      id: "ALT005",
      medicineId: "MED003",
      medicineName: "Aspirin 325mg",
      currentStock: 0,
      threshold: 50,
      category: "Pain Relief",
      supplier: "PharmaCorp",
      lastRestocked: "2024-01-01",
      priority: "High",
      status: "Active",
    },
  ])

  const activeAlerts = alerts.filter((alert) => alert.status === "Active")
  const dismissedAlerts = alerts.filter((alert) => alert.status === "Dismissed")

  const dismissAlert = (id: string) => {
    setAlerts(alerts.map((alert) => (alert.id === id ? { ...alert, status: "Dismissed" } : alert)))
  }

  const reactivateAlert = (id: string) => {
    setAlerts(alerts.map((alert) => (alert.id === id ? { ...alert, status: "Active" } : alert)))
  }

  const getPriorityBadgeVariant = (priority: string) => {
    switch (priority) {
      case "High":
        return "destructive" as const
      case "Medium":
        return "secondary" as const
      case "Low":
        return "outline" as const
      default:
        return "outline" as const
    }
  }

  const getStockStatusColor = (currentStock: number, threshold: number) => {
    if (currentStock === 0) return "text-red-600"
    if (currentStock <= threshold * 0.3) return "text-red-500"
    if (currentStock <= threshold * 0.6) return "text-orange-500"
    return "text-yellow-500"
  }

  const criticalAlerts = activeAlerts.filter((alert) => alert.currentStock === 0)
  const highPriorityAlerts = activeAlerts.filter((alert) => alert.priority === "High" && alert.currentStock > 0)
  const mediumPriorityAlerts = activeAlerts.filter((alert) => alert.priority === "Medium")

  return (
    <div className="space-y-6">
      {/* Summary Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Critical Alerts</p>
                <p className="text-2xl font-bold text-red-600">{criticalAlerts.length}</p>
                <p className="text-xs text-gray-500">Out of stock</p>
              </div>
              <div className="p-3 bg-red-100 rounded-full">
                <AlertTriangle className="h-6 w-6 text-red-600" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">High Priority</p>
                <p className="text-2xl font-bold text-orange-600">{highPriorityAlerts.length}</p>
                <p className="text-xs text-gray-500">Urgent restocking</p>
              </div>
              <div className="p-3 bg-orange-100 rounded-full">
                <TrendingDown className="h-6 w-6 text-orange-600" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Medium Priority</p>
                <p className="text-2xl font-bold text-yellow-600">{mediumPriorityAlerts.length}</p>
                <p className="text-xs text-gray-500">Monitor closely</p>
              </div>
              <div className="p-3 bg-yellow-100 rounded-full">
                <Package className="h-6 w-6 text-yellow-600" />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Critical Alerts */}
      {criticalAlerts.length > 0 && (
        <Alert variant="destructive">
          <AlertTriangle className="h-4 w-4" />
          <AlertDescription>
            <strong>Critical Alert:</strong> {criticalAlerts.length} medicine(s) are completely out of stock and require
            immediate attention.
          </AlertDescription>
        </Alert>
      )}

      {/* Active Alerts */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <AlertTriangle className="h-5 w-5" />
            Active Low Stock Alerts
          </CardTitle>
          <CardDescription>Medicines that require restocking ({activeAlerts.length} active alerts)</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="border rounded-lg">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Medicine</TableHead>
                  <TableHead>Category</TableHead>
                  <TableHead>Current Stock</TableHead>
                  <TableHead>Threshold</TableHead>
                  <TableHead>Supplier</TableHead>
                  <TableHead>Last Restocked</TableHead>
                  <TableHead>Priority</TableHead>
                  <TableHead>Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {activeAlerts.map((alert) => (
                  <TableRow key={alert.id} className={alert.currentStock === 0 ? "bg-red-50" : ""}>
                    <TableCell>
                      <div>
                        <p className="font-medium">{alert.medicineName}</p>
                        <p className="text-sm text-gray-500">{alert.medicineId}</p>
                      </div>
                    </TableCell>
                    <TableCell>{alert.category}</TableCell>
                    <TableCell>
                      <span className={`font-bold ${getStockStatusColor(alert.currentStock, alert.threshold)}`}>
                        {alert.currentStock}
                        {alert.currentStock === 0 && <span className="ml-1 text-xs">(OUT OF STOCK)</span>}
                      </span>
                    </TableCell>
                    <TableCell>{alert.threshold}</TableCell>
                    <TableCell>{alert.supplier}</TableCell>
                    <TableCell>{alert.lastRestocked}</TableCell>
                    <TableCell>
                      <Badge variant={getPriorityBadgeVariant(alert.priority)}>{alert.priority}</Badge>
                    </TableCell>
                    <TableCell>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={() => dismissAlert(alert.id)}
                        className="text-green-600 hover:text-green-700"
                      >
                        <CheckCircle className="h-4 w-4" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>

      {/* Dismissed Alerts */}
      {dismissedAlerts.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <CheckCircle className="h-5 w-5 text-green-600" />
              Dismissed Alerts
            </CardTitle>
            <CardDescription>
              Previously dismissed low stock alerts ({dismissedAlerts.length} dismissed)
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="border rounded-lg">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Medicine</TableHead>
                    <TableHead>Category</TableHead>
                    <TableHead>Stock Level</TableHead>
                    <TableHead>Supplier</TableHead>
                    <TableHead>Priority</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {dismissedAlerts.map((alert) => (
                    <TableRow key={alert.id} className="opacity-60">
                      <TableCell>
                        <div>
                          <p className="font-medium">{alert.medicineName}</p>
                          <p className="text-sm text-gray-500">{alert.medicineId}</p>
                        </div>
                      </TableCell>
                      <TableCell>{alert.category}</TableCell>
                      <TableCell>
                        {alert.currentStock} / {alert.threshold}
                      </TableCell>
                      <TableCell>{alert.supplier}</TableCell>
                      <TableCell>
                        <Badge variant="outline">{alert.priority}</Badge>
                      </TableCell>
                      <TableCell>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => reactivateAlert(alert.id)}
                          className="text-blue-600 hover:text-blue-700"
                        >
                          <X className="h-4 w-4" />
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </div>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
