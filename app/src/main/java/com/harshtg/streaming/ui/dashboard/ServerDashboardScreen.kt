package com.harshtg.streaming.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harshtg.streaming.ui.components.dpadFocusable

data class QuickTool(val name: String, val icon: ImageVector, val description: String)

@Composable
fun ServerDashboardScreen() {
    val quickTools = listOf(
        QuickTool("Fix Metadata", Icons.Default.Build, "IMDb/TMDB/Kitsu Rescan"),
        QuickTool("Scan Manager", Icons.Default.Scanner, "Trigger channel re-index"),
        QuickTool("Failed Files", Icons.Default.ReportProblem, "View skipped channel files"),
        QuickTool("Dead-Link Checker", Icons.Default.LinkOff, "Clean expired TG stream links"),
        QuickTool("Duplicate Finder", Icons.Default.FindInPage, "Purge duplicate video entries"),
        QuickTool("TG Backup", Icons.Default.Backup, "Save to TG Saved Messages")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "Telegram-Stremio Server Dashboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Server Status Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                modifier = Modifier.size(12.dp),
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF2ECC71)
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Server Online", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                        Text(text = "Ping: 24 ms", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusStat("Active Bot Tokens", "8 / 8 Tokens Active")
                        StatusStat("Multi-DB Load", "MongoDB Ready")
                        StatusStat("Indexed Files", "142,850 Videos")
                    }
                }
            }
        }

        // Quick Tools Drawer Title
        item {
            Text(
                text = "Quick Tools Drawer",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Quick Tools Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                quickTools.chunked(2).forEach { rowTools ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        rowTools.forEach { tool ->
                            Card(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(90.dp)
                                    .dpadFocusable(cornerRadius = 12.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = tool.icon,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(text = tool.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(text = tool.description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusStat(title: String, value: String) {
    Column {
        Text(text = title, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}
